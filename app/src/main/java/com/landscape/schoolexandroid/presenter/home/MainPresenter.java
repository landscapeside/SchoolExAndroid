package com.landscape.schoolexandroid.presenter.home;


import android.content.Intent;

import com.landscape.schoolexandroid.presenter.BasePresenter;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface MainPresenter extends BasePresenter {

    void refreshData(Intent data);
}
