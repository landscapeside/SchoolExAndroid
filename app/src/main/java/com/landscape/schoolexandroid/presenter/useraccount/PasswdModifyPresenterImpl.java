package com.landscape.schoolexandroid.presenter.useraccount;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.landscape.netedge.account.IPwd;
import com.landscape.netedge.worktask.IWorkTask;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.LoginActivity;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.useraccount.PwdModifyFragment;
import com.landscape.schoolexandroid.ui.fragment.useraccount.UserModifyFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.useraccount.PasswdModifyView;
import com.utils.behavior.ActivityStack;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;

import javax.inject.Inject;

/**
 * Created by 1 on 2016/7/7.
 */
public class PasswdModifyPresenterImpl implements BasePresenter,IPwd {

    @Inject
    UserAccountDataSource userAccountDataSource;

    PasswdModifyView passwdModifyView;

    IPwd mOptions;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    public PasswdModifyPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        pagerActivity.setToolbarTitle("修改密码");
        passwdModifyView = new PwdModifyFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) passwdModifyView, R.id.contentPanel);
        mOptions = (IPwd) pagerActivity.mProxy.createProxyInstance(this);
        initViews();
    }

    public void initViews() {
        passwdModifyView.setPresenter(this);
        passwdModifyView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                passwdModifyView.setBtnClickListener((oldPwd, newPwd) -> mOptions.modify(oldPwd,newPwd));
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
    public void modify(String oldPwd, String newPwd) {
        userAccountDataSource.modifyPwd(oldPwd, newPwd, new BaseCallBack<BaseBean>(pagerActivity) {
            @Override
            public void response(BaseBean response) {
                mOptions.modifySuc(response);
            }

            @Override
            public void err() {

            }
        });
    }

    @Override
    public void modifySuc(BaseBean baseBean) {
        if (baseBean.isIsSuccess()) {
            userAccountDataSource.clearData();
            pagerActivity.startActivity(new Intent(pagerActivity, LoginActivity.class));
            ActivityStack.finishAllActivity();
        } else {
            ToastUtil.show(pagerActivity,baseBean.getMessage());
        }
    }
}
