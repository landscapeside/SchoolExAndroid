package com.landscape.schoolexandroid.presenter.worktask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.landscape.event.FinishPagerEvent;
import com.landscape.event.RefreshListEvent;
import com.landscape.netedge.worktask.IAnswer;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.datasource.worktask.TaskOptionDataSource;
import com.landscape.schoolexandroid.db.DbHelper;
import com.landscape.schoolexandroid.db.LabelTable;
import com.landscape.schoolexandroid.db.TaskDb;
import com.landscape.schoolexandroid.dialog.AlertDialog;
import com.landscape.schoolexandroid.dialog.CheckDialog;
import com.landscape.schoolexandroid.dialog.PromptDialog;
import com.landscape.schoolexandroid.enums.AnswerMode;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.account.UserFile;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.AnswerFragment;
import com.landscape.schoolexandroid.utils.AnswerUtils;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.landscape.schoolexandroid.views.worktask.AnswerView;
import com.landscape.weight.FlingRelativeLayout;
import com.landscape.weight.ScrollWebView;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Bus;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.tu.crop.BitmapUtil;
import com.tu.crop.CropHelper;
import com.utils.behavior.AppFileUtils;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;
import com.utils.datahelper.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by 1 on 2016/6/30.
 */
public class AnswerPresenterImpl implements BasePresenter, IAnswer,PhotoHelper.PhotoCallbk {

    private static final int REQUEST_LOCATION = 2;

    String promptStrFormat = "你还有%s题未做完\n你确定要现在交卷吗？";
    String checkStrFormat = "完成交卷，点击\"查看记录\"可查看本次做题记录";
    String urlFormat = "HomeWork/Question?id=%s&PapersID=%s&studentid=%s";
    String tmpPic = "tmp.jpg";
    File picTempFile = null, picFile = null;
    Bitmap simpleBitmap = null;

    AnswerView answerView = null;
    AnswerMode answerMode;
    /**
     * data bean
     */
    ExaminationTaskInfo taskInfo;
    List<QuestionInfo> questionInfos = new ArrayList<>();
    int currentQuestion = 0,subjectTypeId = 0;
    IAnswer mOptions;
    PhotoHelper photoHelper;

    /**
     * Views
     */
    PromptDialog promptDialog;
    CheckDialog checkDialog;
    AlertDialog alertDialog;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    @Inject
    UserAccountDataSource userAccountDataSource;
    @Inject
    TaskOptionDataSource taskOptionDataSource;
    @Inject
    Bus mBus;
    @Inject
    BriteDatabase db;

    public AnswerPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        answerView = new AnswerFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) answerView, R.id.contentPanel);
        initViews();
        questionInfos = pagerActivity.getIntent().getParcelableArrayListExtra(Constant.QUESTION_INFO);
        taskInfo = pagerActivity.getIntent().getParcelableExtra(Constant.TASK_INFO);
        subjectTypeId = pagerActivity.getIntent().getIntExtra(Constant.SUBJECT_TYPE_ID, 0);
        answerMode = AnswerMode.getAnswerMode(pagerActivity.getIntent().getIntExtra(Constant.ANSWER_MODE, -1));
        pagerActivity.setToolbarTitle(taskInfo.getName());
        mOptions = (IAnswer) pagerActivity.mProxy.createProxyInstance(this);
        picTempFile = new File(AppFileUtils.getPicsPath(), tmpPic);
        photoHelper = PhotoHelper.getInstance();
        photoHelper.setPhotoCallbk(this);
    }

    public void initViews() {
        answerView.setPresenter(this);
        answerView.setLifeListener(new AnswerView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                answerView.previewTask(
                        AppConfig.BASE_WEB_URL +
                                String.format(urlFormat,
                                        questionInfos.get(currentQuestion).getId(),
                                        taskInfo.getExaminationPapersId(),
                                        userAccountDataSource.getUserAccount().getData().getStudentId()));
                answerView.setDragListener(new ScrollWebView.DragHorizontalListener() {
                    @Override
                    public void leftDrag() {
                        if (currentQuestion - 1 < 0) {
                            ToastUtil.show(pagerActivity, "前面没有了");
                        } else {
                            checkSubmit();
                            answerView.previewTask(
                                    AppConfig.BASE_WEB_URL +
                                            String.format(urlFormat,
                                                    questionInfos.get(--currentQuestion).getId(),
                                                    taskInfo.getExaminationPapersId(),
                                                    userAccountDataSource.getUserAccount().getData().getStudentId()));
                        }
                        answerView.setLocation(currentQuestion + 1, questionInfos.size());
                        answerView.setAnswerCard(questionInfos.get(currentQuestion),subjectTypeId);
                    }

                    @Override
                    public void rightDrag() {
                        if (currentQuestion + 1 >= questionInfos.size()) {
                            submitClick();
                        } else {
                            checkSubmit();
                            answerView.previewTask(
                                    AppConfig.BASE_WEB_URL +
                                            String.format(urlFormat,
                                                    questionInfos.get(++currentQuestion).getId(),
                                                    taskInfo.getExaminationPapersId(),
                                                    userAccountDataSource.getUserAccount().getData().getStudentId()));
                        }
                        answerView.setLocation(currentQuestion + 1, questionInfos.size());
                        answerView.setAnswerCard(questionInfos.get(currentQuestion),subjectTypeId);
                    }
                });

                answerView.setBtnClickListener(new AnswerView.BtnClickListener() {
                    @Override
                    public void finish() {
                        submitClick();
                    }

                    @Override
                    public void location() {
                        Intent intent = new Intent(pagerActivity, PagerActivity.class);
                        intent.putExtra(Constant.PAGER_TYPE, PagerType.QUESTION_LOCATION.getType());
                        intent.putExtra(Constant.LOCATION_INDEX, currentQuestion);
                        intent.putIntegerArrayListExtra(Constant.LOCATION_INFO, (ArrayList<Integer>) AnswerUtils.transforStudentsAnswers(questionInfos));
                        pagerActivity.startActivityForResult(intent, REQUEST_LOCATION);
                    }
                });
                answerView.setLocation(currentQuestion + 1, questionInfos.size());
                if (taskInfo.getDuration() > 0) {
                    answerView.startTimeTick(taskInfo.getDuration());
                } else {
                    alertDialog = new AlertDialog(pagerActivity,"答题时间已过，你已不能继续答题") {
                        @Override
                        public void onOk() {
                            mOptions.finish();
                        }
                    };
                    alertDialog.show();
                }
                answerView.setTimeEnable(taskInfo.getDuration() > 0);
                answerView.setTimeCounterCallbk(() -> {
                    ToastUtil.show(pagerActivity, "时间到，你已不能继续答题");
                    mBus.post(new FinishPagerEvent());
                    mOptions.finish();
                });
                answerView.setAnswerCard(questionInfos.get(currentQuestion),subjectTypeId);
            }

            @Override
            public void onDestroy() {

            }
        });
    }

    @Override
    public void remove() {

    }

    @Override
    public void refreshData(Intent data) {
        if (data.getIntExtra(Constant.INTENT_REQUEST, Constant.INTENT_INVALID_VALUE) != Constant.INTENT_INVALID_VALUE) {
            int requestCode = data.getIntExtra(Constant.INTENT_REQUEST, Constant.INTENT_INVALID_VALUE);
            switch (requestCode) {
                case REQUEST_LOCATION:
                    checkSubmit();
                    currentQuestion = data.getIntExtra(Constant.LOCATION_INDEX, 0);
                    answerView.setLocation(currentQuestion + 1, questionInfos.size());
                    answerView.setAnswerCard(questionInfos.get(currentQuestion),subjectTypeId);
                    answerView.previewTask(
                            AppConfig.BASE_WEB_URL +
                                    String.format(urlFormat,
                                            questionInfos.get(currentQuestion).getId(),
                                            taskInfo.getExaminationPapersId(),
                                            userAccountDataSource.getUserAccount().getData().getStudentId()));
                    break;
                case PhotoHelper.REQUST_DETAIL:
                    PhotoHelper.subcriberView.setTag(R.id.image_file_path, data.getStringExtra(Constant.PIC_PATH));
                    break;
            }
        }
    }

    @Override
    public void back() {
        if (answerMode == AnswerMode.TRAIN) {
            TaskDb.update(db, taskInfo.getStudentQuestionsTasksID(), answerView.getDuration());
            pagerActivity.finish();
        } else {
            submitClick();
        }
    }

    @Override
    public void finish() {
        taskOptionDataSource.endWork(taskInfo, new BaseCallBack<BaseBean>(pagerActivity) {
            @Override
            public void response(BaseBean response) {
                mOptions.finishResult(response);
            }

            @Override
            public void err() {
                mOptions.netErr();
            }
        });
    }

    @Override
    public void finishResult(BaseBean result) {
        if (result.isIsSuccess()) {
            mBus.post(new FinishPagerEvent());
            mBus.post(new RefreshListEvent());
            checkDialog = new CheckDialog(pagerActivity, checkStrFormat) {
                @Override
                public void onOk() {
                    taskInfo.setStatus(TaskStatus.COMPLETE.getStatus());
                    pagerActivity.startActivity(new Intent(pagerActivity, PagerActivity.class)
                            .putExtra(Constant.PAGER_TYPE, PagerType.PREVIEW_TASK.getType())
                            .putExtra(Constant.TASK_INFO, taskInfo));
                    pagerActivity.finish();
                }

                @Override
                public void onCancel() {
                    checkDialog = null;
                    pagerActivity.finish();
                }
            };
            checkDialog.show();
        } else {
            ToastUtil.show(pagerActivity, result.getMessage());
        }
    }

    @Override
    public void uploadFile() {
        userAccountDataSource.uploadFile(picFile, new BaseCallBack<UserFile>(pagerActivity) {
            @Override
            public void response(UserFile response) {
                mOptions.uploadSuc(response);
            }

            @Override
            public void err() {
                mOptions.netErr();
            }
        });
    }

    @Override
    public void uploadSuc(UserFile result) {
        if (result.isIsSuccess()) {
            PhotoHelper.subcriberView.setTag(R.id.image_file_path, picFile.getAbsolutePath());
            PhotoHelper.subcriberView.setTag(R.id.image_url, result.getData());
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(pagerActivity.getContentResolver(), simpleBitmap, null,null));
            PhotoHelper.loadImageIntoSubcriberView(uri);
        } else {
            ToastUtil.show(pagerActivity, result.getMessage());
        }
    }

    @Override
    public void netErr() {

    }

    private void submitClick() {
        checkSubmit();
        promptDialog = new PromptDialog(pagerActivity, String.format(promptStrFormat, AnswerUtils.getUndoQuestionNum(questionInfos))) {
            @Override
            public void onOk() {
                mOptions.finish();
            }
        };
        promptDialog.show();
    }

    private void checkSubmit() {
        if (answerView.isAnswerChanged()) {
            // TODO: 2016/7/3 提交答案
            questionInfos.get(currentQuestion).setStudentsAnswer(answerView.getAnswer());
            taskOptionDataSource.submitAnswer(
                    taskInfo, answerView.getAnswer(),
                    questionInfos.get(currentQuestion), new BaseCallBack<BaseBean>(pagerActivity) {
                        @Override
                        public void response(BaseBean response) {

                        }

                        @Override
                        public void err() {

                        }
                    });
        }
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        simpleBitmap = PhotoHelper.convertToBlackWhite(CropHelper.decodeUriAsBitmap(pagerActivity,uri));
        try {
            picFile = new File(BitmapUtil.saveFile(pagerActivity, simpleBitmap));
            mOptions.uploadFile();
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
    }
}
