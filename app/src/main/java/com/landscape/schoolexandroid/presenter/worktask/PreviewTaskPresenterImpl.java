package com.landscape.schoolexandroid.presenter.worktask;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.landscape.event.FinishPagerEvent;
import com.landscape.netedge.worktask.IWorkTask;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.worktask.TaskOptionDataSource;
import com.landscape.schoolexandroid.db.TaskDb;
import com.landscape.schoolexandroid.enums.AnswerMode;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.worktask.ExaminationPaperListInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.PreviewTaskFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.PreviewTaskView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.sqlbrite.BriteDatabase;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;
import com.utils.datahelper.CollectionUtils;
import com.utils.datahelper.TimeConversion;

import javax.inject.Inject;


/**
 * Created by 1 on 2016/6/27.
 */
public class PreviewTaskPresenterImpl implements BasePresenter,IWorkTask {

    IWorkTask mOptions;

    AnswerMode answerMode;

    @Inject
    TaskOptionDataSource taskOptionDataSource;
    @Inject
    Bus mBus;
    @Inject
    BriteDatabase db;

    String urlFormat = "HomeWork/ExaminationPapers?id=%s&taskid=%s";
    String checkFormat = "HomeWork/CheckPaper?StudentQuestionsTasksId=%s";
    ExaminationTaskInfo taskInfo;
    PreviewTaskView previewTaskView;
    /**
     * parent
     */
    PagerActivity pagerActivity;


    public PreviewTaskPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        taskInfo = pagerActivity.getIntent().getParcelableExtra(Constant.TASK_INFO);
        if (taskInfo == null) {
            throw new IllegalArgumentException("taskinfo为空");
        }
        /**
         * 1.将duration扩展为秒单位
         * 2.
         * 如果设置了分钟数 就可以视为 答题模式
         * 答题模式 不允许退出  如果点击“返回”按钮就提示他是否交卷
         * 倒计时为：答题开始时间+分钟数-当前时间（StartTime+Duration-  DateTime.Now） 不要用Duration直接倒计时 以免系统意外终止 下次进入的时候倒计时不准确
         * 如果StartTime为空则取当前时间
         *
         * 如果没有设置分钟数（Duration=null 或者Duration=0） 倒计时为：为普通模式
         * 为普通模式  允许退出  如果点击“返回”按钮就可以返回
         * 答题结束时间-当前时间 （CanEndDateTime-DateTime.Now）
         * */
        taskInfo.setDuration(taskInfo.getDuration()*60);
        if (taskInfo.getDuration() == 0
                && taskInfo.isIsTasks()) {
            // 普通模式
            answerMode = AnswerMode.TRAIN;
            taskInfo.setDuration(TimeConversion.getDurationByEnd(taskInfo.getCanEndDateTime()));
        } else {
            // 答题模式
            answerMode = AnswerMode.EXAM;
            taskInfo.setDuration(TimeConversion.getDurationByStart(
                    taskInfo.getStartTime(),taskInfo.getDuration()));
        }
        pagerActivity.setToolbarTitle(taskInfo.getName());
        previewTaskView = new PreviewTaskFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) previewTaskView, R.id.contentPanel);
        mOptions = (IWorkTask) pagerActivity.mProxy.createProxyInstance(this);
        mBus.register(this);
        initViews();
        TaskDb.query(db, taskInfo.getStudentQuestionsTasksID(), duration -> {
            if (!TextUtils.isEmpty(duration)) {
                taskInfo.setDuration(Integer.parseInt(duration));
            }
        });
    }

    public void initViews() {
        previewTaskView.setPresenter(this);
        previewTaskView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                previewTaskView.setClickListener(() -> {
                    taskOptionDataSource.startWork(taskInfo, new BaseCallBack<BaseBean>(pagerActivity) {
                        @Override
                        public void response(BaseBean response) {

                        }

                        @Override
                        public void err() {

                        }
                    });
                    mOptions.getExaminationPaper(taskInfo);

                });
                if (CollectionUtils.isIn(
                        TaskStatus.getStatus(taskInfo.getStatus()),
                        TaskStatus.COMPLETE,
                        TaskStatus.READED)) {
                    previewTaskView.previewTask(
                            AppConfig.BASE_WEB_URL +
                                    String.format(checkFormat,
                                            taskInfo.getStudentQuestionsTasksID()));
                } else {
                    previewTaskView.previewTask(
                            AppConfig.BASE_WEB_URL +
                                    String.format(urlFormat,
                                            taskInfo.getExaminationPapersId(),
                                            taskInfo.getStudentQuestionsTasksID()));
                }
                previewTaskView.startEnable(CollectionUtils.isIn(
                        TaskStatus.getStatus(taskInfo.getStatus()),
                        TaskStatus.INIT,
                        TaskStatus.RUN)&&taskInfo.isIsTasks());
            }

            @Override
            public void onDestroy() {

            }
        });
    }

    @Override
    public void remove() {
        mBus.unregister(this);
    }

    @Override
    public void refreshData(Intent data) {

    }

    @Override
    public void back() {
        pagerActivity.finish();
    }

    @Override
    public void netErr() {

    }

    @Override
    public void getExaminationPaper(ExaminationTaskInfo taskInfo) {
        taskOptionDataSource.getPaper(taskInfo, new BaseCallBack<ExaminationPaperListInfo>(pagerActivity) {
            @Override
            public void response(ExaminationPaperListInfo response) {
                mOptions.paperResult(response);
            }

            @Override
            public void err() {
                mOptions.netErr();
            }
        });
    }

    @Override
    public void paperResult(ExaminationPaperListInfo result) {
        if (result.isIsSuccess()) {
            Intent intent = new Intent(pagerActivity, PagerActivity.class).putExtra(Constant.PAGER_TYPE, PagerType.ANSWER.getType());
            intent = taskOptionDataSource.putWorkData(
                    intent,
                    result.getData().get(0).getQuestionGruop(),
                    taskInfo,
                    result.getData().get(0).getSubjectTypeId());
            intent.putExtra(Constant.ANSWER_MODE, answerMode.getCode());
            pagerActivity.startActivity(intent);
        } else {
            ToastUtil.show(pagerActivity,result.getMessage());
        }
    }

    @Subscribe
    public void onFinishAnswer(FinishPagerEvent event) {
        pagerActivity.finish();
    }
}
