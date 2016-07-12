package com.landscape.schoolexandroid.presenter.mistake;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.mode.mistake.MistakeInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.PreviewTaskFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.PreviewTaskView;
import com.utils.behavior.FragmentsUtils;

/**
 * Created by 1 on 2016/7/12.
 */
public class PreviewMistakePresenterImpl implements BasePresenter {

    String mistakeUrl = "HomeWork/ErrorQustionAnswer?QuestionID=%s&StudentQuestionsTasksId=%s";

    MistakeInfo mistakeInfo;
    PreviewTaskView previewTaskView;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    public PreviewMistakePresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        mistakeInfo = pagerActivity.getIntent().getParcelableExtra(Constant.MISTAKE_INFO);
        if (mistakeInfo == null) {
            throw new IllegalArgumentException("mistakeInfo为空");
        }
        pagerActivity.setToolbarTitle(mistakeInfo.getName());
        previewTaskView = new PreviewTaskFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) previewTaskView, R.id.contentPanel);
        initViews();
    }

    public void initViews() {
        previewTaskView.setPresenter(this);
        previewTaskView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                previewTaskView.startEnable(false);
                previewTaskView.previewTask(
                        AppConfig.BASE_WEB_URL +
                        String.format(mistakeUrl,
                                "",
                                mistakeInfo.getStudentQuestionsTasksID()));
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
}
