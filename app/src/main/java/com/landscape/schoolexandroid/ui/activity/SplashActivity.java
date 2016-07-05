package com.landscape.schoolexandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.utils.datahelper.RxCounter;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    @Inject
    UserAccountDataSource userAccountDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((BaseApp)getApplication()).getAppComponent().inject(this);
        RxCounter.counter(3, 0).doOnCompleted(this::next).subscribe();
    }

    public void next(){
        if (userAccountDataSource.getUserAccount() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(this,MainSlideMenuActivity.class));
            finish();
        }
    }
}
