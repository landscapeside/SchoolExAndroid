package com.landscape.schoolexandroid.presenter.useraccount;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.netedge.account.IFeedbk;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.dialog.LogoutDialog;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.LoginActivity;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.useraccount.FeedbkFragment;
import com.landscape.schoolexandroid.ui.fragment.useraccount.UserModifyFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.useraccount.FeedBkView;
import com.landscape.schoolexandroid.views.useraccount.UserModifyView;
import com.utils.behavior.ActivityStack;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;

import javax.inject.Inject;

/**
 * Created by 1 on 2016/7/7.
 */
public class FeedBkPresenterImpl implements BasePresenter,IFeedbk {

    @Inject
    UserAccountDataSource userAccountDataSource;

    FeedBkView feedBkView;
    IFeedbk mOptions;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    public FeedBkPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        pagerActivity.setToolbarTitle("反馈信息");
        feedBkView = new FeedbkFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) feedBkView, R.id.contentPanel);
        mOptions = (IFeedbk) pagerActivity.mProxy.createProxyInstance(this);
        initViews();
    }

    public void initViews() {
        feedBkView.setPresenter(this);
        feedBkView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                feedBkView.setBtnClickListener((content, type) -> mOptions.submit(content,type));
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
    public void submit(String content, String type) {
        userAccountDataSource.submitFeedBk(content, type, new BaseCallBack<BaseBean>(pagerActivity) {
            @Override
            public void response(BaseBean response) {
                mOptions.submitSuc(response);
            }

            @Override
            public void err() {

            }
        });
    }

    @Override
    public void submitSuc(BaseBean result) {
        if (result.isIsSuccess()) {
            back();
        } else {
            ToastUtil.show(pagerActivity,result.getMessage());
        }
    }
}
