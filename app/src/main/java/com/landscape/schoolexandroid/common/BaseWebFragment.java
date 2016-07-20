package com.landscape.schoolexandroid.common;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.landscape.schoolexandroid.R;
import com.landscape.weight.FlingRelativeLayout;
import com.landscape.weight.ScrollWebView;

import java.lang.reflect.Field;

/**
 * Created by 1 on 2016/6/1.
 */
public abstract class BaseWebFragment extends BaseFragment implements View.OnClickListener{
    protected LinearLayout mErrorLayout;
    protected ScrollWebView mWebView;
    protected ProgressBar mProgressBar;
    protected View note_online_error;
    protected FlingRelativeLayout webViewLayout;
    private boolean isError = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initWebView(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.note_online_error:
                hideErrorPage();
                refresh();
                break;
        }
    }

    protected WebView initWebView(View view) {
        webViewLayout = (FlingRelativeLayout) view.findViewById(R.id.weblayout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);
        mWebView = (ScrollWebView) view.findViewById(R.id.webView);
        mWebView.canGoBack();
        mErrorLayout =(LinearLayout)view.findViewById(R.id.web_error_layout);
        note_online_error = view.findViewById(R.id.note_online_error);
        note_online_error.setOnClickListener(this);
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true); // 支持缩放
        settings.setBuiltInZoomControls(true); // 启用内置缩放装置
        settings.setJavaScriptEnabled(true); // 启用JS脚本
        settings.setLoadWithOverviewMode(true);//
        settings.setUseWideViewPort(true);// 设置概览模式
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setDatabaseEnabled(true);
        String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        settings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        settings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        settings.setDomStorageEnabled(true);

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         */
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        if(Build.VERSION.SDK_INT>=11){
            settings.setDisplayZoomControls(false);
        }else{
            setZoomControlGone(mWebView);
        }
        //target 23 default false, so manual set true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO: 2016/6/1 处理h5与native交互
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showErrorPage();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            // 当WebView进度改变时更新窗口进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    if(!isError){
                        mWebView.setVisibility(View.VISIBLE);
                        onLoadSuc();
                    }
                } else {
                    if (mProgressBar.getVisibility() != View.VISIBLE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
            }


            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }


        });

        return mWebView;
    }

    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    protected void showErrorPage() {
        isError = true;
        mWebView.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }
    protected void hideErrorPage() {
        isError = false;
        mErrorLayout.setVisibility(View.GONE);
    }

    public abstract void refresh();

    public void onLoadSuc(){}
}
