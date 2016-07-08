package com.landscape.schoolexandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.jsware.draglayout.DragContentLayout;
import com.jsware.draglayout.DragLayout;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.landscape.schoolexandroid.presenter.home.MainPresenterImpl;
import com.tu.crop.CropHelper;
import com.utils.behavior.FragmentsUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    MainPresenterImpl presenter = null;

    @Bind(R.id.drag_menu)
    RelativeLayout dragMenu;
    @Bind(R.id.drag_content)
    DragContentLayout dragContent;
    @Bind(R.id.dl)
    public DragLayout dl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (presenter == null) {
            presenter = new MainPresenterImpl(this);
        } else {
            presenter.refreshData(intent);
        }
    }

    private void initViews() {
        // TODO init views
        presenter = new MainPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentsUtils.unInstall();
        RetrofitService.cancel();
        ButterKnife.unbind(this);
        presenter.remove();
        CropHelper.cleanAllCropCache(getApplicationContext());
    }
}
