package com.landscape.schoolexandroid.presenter.worktask;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.event.FinishPagerEvent;
import com.landscape.event.RefreshListEvent;
import com.landscape.netedge.worktask.IAnswer;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.datasource.worktask.TaskOptionDataSource;
import com.landscape.schoolexandroid.dialog.PromptDialog;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.worktask.ExaminationPaperInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.AnswerFragment;
import com.landscape.schoolexandroid.utils.AnswerUtils;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.AnswerView;
import com.landscape.weight.FlingRelativeLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;
import com.utils.datahelper.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by 1 on 2016/6/30.
 */
public class AnswerPresenterImpl implements BasePresenter,IAnswer {

    private static final int REQUEST_LOCATION = 2;

    String urlFormat = "HomeWork/Question?id=%s&PapersID=%s&studentid=%s";

    AnswerView answerView = null;
    /**
     * data bean
     */
    ExaminationTaskInfo taskInfo;
    List<QuestionInfo> questionInfos = new ArrayList<>();
    int currentQuestion = 0 ;
    IAnswer mOptions;

    /**
     * Views
     * */
    PromptDialog promptDialog;

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

    public AnswerPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        answerView = new AnswerFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) answerView, R.id.contentPanel);
        initViews();
        questionInfos = pagerActivity.getIntent().getParcelableArrayListExtra(Constant.QUESTION_INFO);
        taskInfo = pagerActivity.getIntent().getParcelableExtra(Constant.TASK_INFO);
        pagerActivity.setToolbarTitle(taskInfo.getName());
        mOptions = (IAnswer) pagerActivity.mProxy.createProxyInstance(this);
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
                answerView.setFlingListener(new FlingRelativeLayout.FlingListener() {
                    @Override
                    public void prev() {
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
                        answerView.setLocation(currentQuestion+1,questionInfos.size());
                        answerView.setAnswerCard(questionInfos.get(currentQuestion));
                    }

                    @Override
                    public void next() {
                        if (currentQuestion + 1 >= questionInfos.size()) {
                            ToastUtil.show(pagerActivity, "后面没有了");
                        } else {
                            checkSubmit();
                            answerView.previewTask(
                                    AppConfig.BASE_WEB_URL +
                                            String.format(urlFormat,
                                                    questionInfos.get(++currentQuestion).getId(),
                                                    taskInfo.getExaminationPapersId(),
                                                    userAccountDataSource.getUserAccount().getData().getStudentId()));
                        }
                        answerView.setLocation(currentQuestion+1,questionInfos.size());
                        answerView.setAnswerCard(questionInfos.get(currentQuestion));
                    }
                });
                answerView.setBtnClickListener(new AnswerView.BtnClickListener() {
                    @Override
                    public void finish() {
                        promptDialog = new PromptDialog(pagerActivity,"确定要交卷吗？") {
                            @Override
                            public void onOk() {
                                mOptions.finish();
                            }
                        };
                        promptDialog.show();
                    }

                    @Override
                    public void location() {
                        Intent intent = new Intent(pagerActivity, PagerActivity.class);
                        intent.putExtra(Constant.PAGER_TYPE, PagerType.QUESTION_LOCATION.getType());
                        intent.putExtra(Constant.LOCATION_INDEX, currentQuestion);
                        intent.putIntegerArrayListExtra(Constant.LOCATION_INFO, (ArrayList<Integer>) AnswerUtils.transforStudentsAnswers(questionInfos));
                        pagerActivity.startActivityForResult(intent,REQUEST_LOCATION);
                    }
                });
                answerView.setLocation(currentQuestion+1,questionInfos.size());
                answerView.startTimeTick(taskInfo.getDuration());
//                answerView.setEnable(CollectionUtils.isIn(
//                            TaskStatus.getStatus(taskInfo.getStatus()),
//                            TaskStatus.INIT,
//                            TaskStatus.RUN)&&!taskInfo.isIsTasks() && taskInfo.getDuration()>0);
                answerView.setTimeCounterCallbk(() -> {
                    ToastUtil.show(pagerActivity,"时间到，你已不能继续答题");
                    mBus.post(new FinishPagerEvent());
                    mOptions.finish();
                });
                answerView.setAnswerCard(questionInfos.get(currentQuestion));
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
                    currentQuestion = data.getIntExtra(Constant.LOCATION_INDEX, 0);
                    answerView.setLocation(currentQuestion+1,questionInfos.size());
                    answerView.previewTask(
                            AppConfig.BASE_WEB_URL +
                                    String.format(urlFormat,
                                            questionInfos.get(currentQuestion).getId(),
                                            taskInfo.getExaminationPapersId(),
                                            userAccountDataSource.getUserAccount().getData().getStudentId()));
                    break;
                case PhotoHelper.SERVER_CAPTURE_PHOTO:

                    break;
                case PhotoHelper.SERVER_SELECT_PHOTO:

                    break;
                case PhotoHelper.SERVER_CROP_PHOTO:

                    break;
            }
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
            pagerActivity.finish();
        } else {
            ToastUtil.show(pagerActivity,result.getMessage());
        }
    }

    @Override
    public void netErr() {

    }

    private void checkSubmit() {
        if (answerView.isAnswerChanged()) {
            // TODO: 2016/7/3 提交答案
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
}
