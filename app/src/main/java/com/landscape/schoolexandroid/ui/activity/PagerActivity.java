package com.landscape.schoolexandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.presenter.BasePresenter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PagerActivity extends BaseActivity {

    BasePresenter presenter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar_right)
    TextView toolbarRight;
    @Bind(R.id.toolbar_right_second)
    ImageView toolbarRightImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        initPresenter();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (presenter == null) {
            initPresenter();
        } else {
            presenter.refreshData(intent);
        }
    }

    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    public void showRightBtn(String btnText, View.OnClickListener clickListener) {
        toolbarRight.setText(btnText);
        toolbarRight.setVisibility(View.VISIBLE);
        toolbarRight.setOnClickListener(clickListener);
    }

    public void setRightBtnEnabled(boolean enabled) {
        toolbarRight.setVisibility(View.VISIBLE);
        toolbarRight.setEnabled(enabled);
        toolbarRight.setClickable(enabled);
    }

    public void showRightImgBtn(View.OnClickListener clickListener) {
        toolbarRightImg.setVisibility(View.VISIBLE);
        toolbarRightImg.setOnClickListener(clickListener);
    }

    private void initPresenter() {
        int type = getIntent().getIntExtra(Constant.PAGER_TYPE, -1);
        PagerType pagerType = PagerType.getType(type);
        if (pagerType != PagerType.NONE) {
            Class presenterCls = pagerType.getTypeCls();
            Class[] paramsTypes = new Class[]{PagerActivity.class};
            Object[] params = new Object[]{this};
            try {
                Constructor constructor = presenterCls.getConstructor(paramsTypes);
                presenter = (BasePresenter) constructor.newInstance(params);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("需要提供一个接受BaseActivity的presenter构造函数");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("IllegalArgumentException in PagerActivity");
        }
    }

    @Override
    public void onBackPressed() {
        presenter.back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.remove();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (presenter == null) {
                initPresenter();
            } else {
                data.putExtra(Constant.INTENT_REQUEST, requestCode);
                presenter.refreshData(data);
            }
        }
    }
}
