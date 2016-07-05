package com.landscape.schoolexandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.landscape.schoolexandroid.presenter.home.MainPresenterImpl;
import com.landscape.schoolexandroid.presenter.home.MainSlideMenuPresenterImpl;
import com.utils.behavior.FragmentsUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainSlideMenuActivity extends BaseActivity {

    MainSlideMenuPresenterImpl presenter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_slide_menu);
        ButterKnife.bind(this);
        initPresenter();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (presenter == null) {
            presenter = new MainSlideMenuPresenterImpl(this);
        } else {
            presenter.refreshData(intent);
        }
    }

    private void initPresenter() {
        presenter = new MainSlideMenuPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentsUtils.unInstall();
        RetrofitService.cancel();
        ButterKnife.unbind(this);
        presenter.remove();
    }
}
