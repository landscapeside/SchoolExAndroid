package com.landscape.schoolexandroid.presenter;

/**
 * Created by Administrator on 2016/4/25.
 */
public interface BasePresenter {
    default void start() {}
    default void stop(){}
    default void remove(){}
}
