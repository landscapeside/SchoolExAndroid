package com.landscape.schoolexandroid.presenter.worktask;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.worktask.ExaminationPaperInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.AnswerFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.AnswerView;
import com.landscape.weight.FlingRelativeLayout;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;
import com.utils.datahelper.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by 1 on 2016/6/30.
 */
public class AnswerPresenterImpl implements BasePresenter {

    String urlFormat = "HomeWork/Question?id=%s&PapersID=%s&studentid=%s";

    AnswerView answerView = null;
    /**
     * data bean
     */
    ExaminationTaskInfo taskInfo;
    List<QuestionInfo> questionInfos = new ArrayList<>();
    int currentQuestion = 0 ;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    @Inject
    UserAccountDataSource userAccountDataSource;

    public AnswerPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        answerView = new AnswerFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) answerView, R.id.contentPanel);
        initViews();
        questionInfos = pagerActivity.getIntent().getParcelableArrayListExtra(Constant.QUESTION_INFO);
        taskInfo = pagerActivity.getIntent().getParcelableExtra(Constant.TASK_INFO);
        pagerActivity.setToolbarTitle(taskInfo.getName());
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
                            answerView.previewTask(
                                    AppConfig.BASE_WEB_URL +
                                            String.format(urlFormat,
                                                    questionInfos.get(--currentQuestion).getId(),
                                                    taskInfo.getExaminationPapersId(),
                                                    userAccountDataSource.getUserAccount().getData().getStudentId()));
                        }
                        answerView.setLocation(currentQuestion+1,questionInfos.size());
                    }

                    @Override
                    public void next() {
                        if (currentQuestion + 1 >= questionInfos.size()) {
                            ToastUtil.show(pagerActivity, "后面没有了");
                        } else {
                            answerView.previewTask(
                                    AppConfig.BASE_WEB_URL +
                                            String.format(urlFormat,
                                                    questionInfos.get(++currentQuestion).getId(),
                                                    taskInfo.getExaminationPapersId(),
                                                    userAccountDataSource.getUserAccount().getData().getStudentId()));
                        }
                        answerView.setLocation(currentQuestion+1,questionInfos.size());
                    }
                });
                answerView.setLocation(currentQuestion+1,questionInfos.size());
                answerView.startTimeTick(taskInfo.getDuration());
                answerView.setEnable(CollectionUtils.isIn(
                            TaskStatus.getStatus(taskInfo.getStatus()),
                            TaskStatus.INIT,
                            TaskStatus.RUN)&&!taskInfo.isIsTasks() && taskInfo.getDuration()>0);
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
}
