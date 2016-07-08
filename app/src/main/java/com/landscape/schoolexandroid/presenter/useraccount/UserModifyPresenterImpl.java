package com.landscape.schoolexandroid.presenter.useraccount;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.dialog.LogoutDialog;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.LoginActivity;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.useraccount.UserModifyFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.useraccount.UserModifyView;
import com.utils.behavior.ActivityStack;
import com.utils.behavior.FragmentsUtils;

import javax.inject.Inject;

/**
 * Created by 1 on 2016/7/7.
 */
public class UserModifyPresenterImpl implements BasePresenter {

    @Inject
    UserAccountDataSource userAccountDataSource;

    UserModifyView userModifyView;
    LogoutDialog logoutDialog;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    public UserModifyPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        pagerActivity.setToolbarTitle("个人信息");
        userModifyView = new UserModifyFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) userModifyView, R.id.contentPanel);
        initViews();
    }

    public void initViews() {
        userModifyView.setPresenter(this);
        userModifyView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                userModifyView.refreshUserInfo(userAccountDataSource.getUserAccount());
                userModifyView.setBtnClickListener(new UserModifyView.BtnClickListener() {
                    @Override
                    public void quit() {
                        logoutDialog = new LogoutDialog(pagerActivity,"确定退出吗？") {
                            @Override
                            public void onOk() {
                                userAccountDataSource.clearData();
                                pagerActivity.startActivity(new Intent(pagerActivity, LoginActivity.class));
                                ActivityStack.finishAllActivity();
                            }
                        };
                        logoutDialog.show();
                    }

                    @Override
                    public void passwd() {
                        pagerActivity.startActivity(new Intent(pagerActivity,PagerActivity.class)
                                .putExtra(Constant.PAGER_TYPE, PagerType.PWD_MODIFY.getType()));
                    }
                });
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
