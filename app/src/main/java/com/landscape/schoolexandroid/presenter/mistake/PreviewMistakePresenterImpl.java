package com.landscape.schoolexandroid.presenter.mistake;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.netedge.mistake.IMistake;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.home.MistakeDataSource;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.mistake.MistakeInfo;
import com.landscape.schoolexandroid.mode.mistake.MistakeQuestionBean;
import com.landscape.schoolexandroid.mode.mistake.MistakeQuestionListInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.PreviewTaskFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.PreviewTaskView;
import com.landscape.weight.FlingRelativeLayout;
import com.landscape.weight.ScrollWebView;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;

import javax.inject.Inject;

/**
 * Created by 1 on 2016/7/12.
 */
public class PreviewMistakePresenterImpl implements BasePresenter,IMistake {

    IMistake mOptions;

    @Inject
    MistakeDataSource mistakeDataSource;

    String mistakeUrl = "HomeWork/ErrorQustionAnswer?QuestionID=%s&StudentQuestionsTasksId=%s";

    MistakeInfo mistakeInfo;
    PreviewTaskView previewTaskView;
    MistakeQuestionListInfo mistakeQuestionListInfo;
    int currentQuestion = 0;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    public PreviewMistakePresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        mistakeInfo = pagerActivity.getIntent().getParcelableExtra(Constant.MISTAKE_INFO);
        if (mistakeInfo == null) {
            throw new IllegalArgumentException("mistakeInfo为空");
        }
        pagerActivity.setToolbarTitle(mistakeInfo.getName());
        previewTaskView = new PreviewTaskFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) previewTaskView, R.id.contentPanel);
        initViews();
        mOptions = (IMistake) pagerActivity.mProxy.createProxyInstance(this);
        mOptions.getQuestion(mistakeInfo.getStudentQuestionsTasksID());
    }

    public void initViews() {
        previewTaskView.setPresenter(this);
        previewTaskView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                previewTaskView.setDragListener(new ScrollWebView.DragHorizontalListener() {
                    @Override
                    public void leftDrag() {
                        if (currentQuestion - 1 < 0) {
                            ToastUtil.show(pagerActivity, "前面没有了");
                        } else {
                            currentQuestion--;
                            refreshPager();
                        }
                    }

                    @Override
                    public void rightDrag() {
                        if (currentQuestion + 1 >= mistakeQuestionListInfo.getErrorList().size()) {
                            ToastUtil.show(pagerActivity, "后面没有了");
                        } else {
                            currentQuestion++;
                            refreshPager();
                        }
                    }
                });
                previewTaskView.startEnable(false);
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
    public void back() {
        pagerActivity.finish();
    }

    @Override
    public void netErr() {

    }

    @Override
    public void getQuestion(int taskId) {
        mistakeDataSource.getQuestion(taskId, new BaseCallBack<MistakeQuestionBean>(pagerActivity) {
            @Override
            public void response(MistakeQuestionBean response) {
                mOptions.getQuestionSuc(response);
            }

            @Override
            public void err() {

            }
        });
    }

    @Override
    public void getQuestionSuc(MistakeQuestionBean result) {
        if (result.isIsSuccess()) {
            mistakeQuestionListInfo = result.getData();
            currentQuestion = 0;
            refreshPager();
        } else {
            ToastUtil.show(pagerActivity,result.getMessage());
        }
    }

    private void refreshPager() {
        if (currentQuestion >= mistakeQuestionListInfo.getErrorList().size()) {
            return;
        }
        previewTaskView.previewTask(
                AppConfig.BASE_WEB_URL +
                        String.format(mistakeUrl,
                                mistakeQuestionListInfo.getErrorList().get(currentQuestion).getQuerstionId(),
                                mistakeInfo.getStudentQuestionsTasksID()));
    }
}
