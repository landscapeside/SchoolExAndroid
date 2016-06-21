package com.landscape.schoolexandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.utils.datahelper.RxCounter;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RxCounter.counter(5, 0).doOnCompleted(this::next);
    }

    public void next(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
