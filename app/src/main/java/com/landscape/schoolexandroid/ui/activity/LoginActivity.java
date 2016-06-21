package com.landscape.schoolexandroid.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.landscape.netedge.account.ILogin;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.LoginApi;
import com.landscape.schoolexandroid.api.RxService;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.utils.behavior.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements ILogin {

    ILogin mOptions;

    @Bind(R.id.edit_username)
    EditText editUsername;
    @Bind(R.id.edit_passwd)
    EditText editPasswd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mOptions = (ILogin) mProxy.createProxyInstance(ILogin.class);
    }

    private boolean check() {
        if (TextUtils.isEmpty(editUsername.getText().toString().trim())) {
            ToastUtil.show(this,editUsername.getHint());
            return false;
        }
        if (TextUtils.isEmpty(editPasswd.getText().toString().trim())) {
            ToastUtil.show(this,editPasswd.getHint());
            return false;
        }
        return true;
    }

    @OnClick(R.id.login)
    void login(View view){
        //TODO
        if (check()) {
            mOptions.login();
        }
    }

    @Override
    public void login() {
        RxService.createApi(LoginApi.class)
                .accountLogin(editUsername.getText().toString().trim(),editPasswd.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    RxService.netErr(throwable);
                }).onErrorResumeNext(Observable.empty())
                .subscribe(mOptions::loginResult);
    }

    @Override
    public void loginResult(String result){

    }
}
