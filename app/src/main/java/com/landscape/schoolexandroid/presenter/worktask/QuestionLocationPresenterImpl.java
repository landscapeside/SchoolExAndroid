package com.landscape.schoolexandroid.presenter.worktask;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.QuestionLocationFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.QuestionLocationView;
import com.orhanobut.logger.Logger;
import com.utils.behavior.FragmentsUtils;

/**
 * Created by landscape on 2016/6/29.
 */
public class QuestionLocationPresenterImpl implements BasePresenter {

    QuestionLocationView locationView = null;
    /**
     * parent
     */
    PagerActivity pagerActivity;

    public QuestionLocationPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        locationView = new QuestionLocationFragment();
        pagerActivity.setToolbarTitle("题目");
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) locationView, R.id.contentPanel);
        initViews();
    }

    public void initViews() {
        locationView.setPresenter(this);
        locationView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                locationView.setListItemSelectListener(position -> {
                    // TODO: 2016/6/30 重定位

                });
                locationView.listData(pagerActivity.getIntent().getIntegerArrayListExtra(Constant.LOCATION_INFO));
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
