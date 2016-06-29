package com.landscape.schoolexandroid.presenter.worktask;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.netedge.worktask.IWorkTask;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.worktask.TaskOptionDataSource;
import com.landscape.schoolexandroid.mode.worktask.ExaminationPaperListInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.PreviewTaskFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.PreviewTaskView;
import com.orhanobut.logger.Logger;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;

import javax.inject.Inject;


/**
 * Created by 1 on 2016/6/27.
 */
public class PreviewTaskPresenterImpl implements BasePresenter,IWorkTask {

    IWorkTask mOptions;

    @Inject
    TaskOptionDataSource taskOptionDataSource;

    String urlFormat = "HomeWork/ExaminationPapers?id=%s&taskid=%s";
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
        pagerActivity.setToolbarTitle(taskInfo.getName());
        previewTaskView = new PreviewTaskFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) previewTaskView, R.id.contentPanel);
        mOptions = (IWorkTask) pagerActivity.mProxy.createProxyInstance(this);
        initViews();
    }

    public void initViews() {
        previewTaskView.setPresenter(this);
        previewTaskView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                previewTaskView.setClickListener(() -> {
                    taskOptionDataSource.startWork(taskInfo, new BaseCallBack<String>(pagerActivity) {
                        @Override
                        public void response(String response) {
                            Logger.i(response);
                        }

                        @Override
                        public void err() {

                        }
                    });
                    mOptions.getExaminationPaper(taskInfo);

                });
                previewTaskView.previewTask(
                        AppConfig.BASE_WEB_URL +
                                String.format(urlFormat,
                                        taskInfo.getExaminationPapersId(),
                                        taskInfo.getStudentQuestionsTasksID()));
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

        } else {
            ToastUtil.show(pagerActivity,result.getMessage());
        }
    }
}
