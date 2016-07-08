package com.landscape.schoolexandroid.presenter.useraccount;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.PreviewTaskFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.PreviewTaskView;
import com.utils.behavior.FragmentsUtils;
import com.utils.datahelper.CollectionUtils;

import javax.inject.Inject;


/**
 * Created by 1 on 2016/6/27.
 */
public class ChartPresenterImpl implements BasePresenter {

    public static final int FLOWERS = 897;
    public static final int WONDER = 881;

    int type = -1;

    String flowerFormat = "Report/ReportFlower?ID=%s";
    String wonderFormat = "Report/ReportAppraisal?ID=%s";
    PreviewTaskView previewTaskView;

    @Inject
    UserAccountDataSource userAccountDataSource;

    /**
     * parent
     */
    PagerActivity pagerActivity;


    public ChartPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        pagerActivity.setToolbarTitle(pagerActivity.getIntent().getStringExtra(Constant.PAGER_TITLE));
        type = pagerActivity.getIntent().getIntExtra(Constant.CHART_TYPE, -1);
        previewTaskView = new PreviewTaskFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) previewTaskView, R.id.contentPanel);
        initViews();
    }

    public void initViews() {
        previewTaskView.setPresenter(this);
        previewTaskView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {

                switch (type) {
                    case FLOWERS:
                        previewTaskView.previewTask(
                                AppConfig.BASE_WEB_URL +
                                        String.format(flowerFormat,
                                                userAccountDataSource.getUserAccount().getData().getStudentId()));
                        break;
                    case WONDER:
                        previewTaskView.previewTask(
                                AppConfig.BASE_WEB_URL +
                                        String.format(wonderFormat,
                                                userAccountDataSource.getUserAccount().getData().getStudentId()));
                        break;
                }
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

}
