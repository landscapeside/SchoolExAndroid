package com.landscape.schoolexandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.utils.datahelper.RxCounter;

public class SplashActivity extends BaseActivity {

    UserAccountDataSource userAccountDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userAccountDataSource = new UserAccountDataSource(this);
        RxCounter.counter(3, 0).doOnCompleted(this::next).subscribe();
    }

    public void next(){
        if (userAccountDataSource.getUserAccount() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}
