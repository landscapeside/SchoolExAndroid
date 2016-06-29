package com.landscape.schoolexandroid.presenter;

import android.content.Intent;

/**
 * Created by Administrator on 2016/4/25.
 */
public interface BasePresenter {
    default void start() {}
    default void stop(){}
    void remove();
    void refreshData(Intent data);
}
