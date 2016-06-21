package com.landscape.schoolexandroid.views;

import android.view.View;

import com.landscape.schoolexandroid.presenter.BasePresenter;

/**
 * Created by Administrator on 2016/4/25.
 */
public interface BaseView<T extends BasePresenter> {
    interface ViewLifeListener{
        void onInitialized();
        void onDestroy();
        default void onPause(){}
    }
    void setPresenter(T presenter);
    int getResId();
    void setLifeListener(ViewLifeListener lifeListener);
    default View getView(){
        return null;
    }
    default View getView(int resId) {
        return null;
    }
    default BaseView setBGMDrawRes(int drawableId) {
        return this;
    }
    default int getBGMDrawRes() {
        return -1;
    }
    default BaseView setValid(boolean valid) {
        return this;
    }
    default boolean isValid() {
        return true;
    }
    default BaseView setVisible(boolean visible) {
        return this;
    }
    default boolean isVisible() {
        return true;
    }
    default BaseView setTag(Object tag) {
        return this;
    }
    default <T extends Object> T getTag() {
        return null;
    }
    default BaseView loadAnim(int resId) {
        return this;
    }
    default BaseView finishLoadAnim() {
        return this;
    }
    default BaseView tip(View.OnClickListener onClickListener) {
        return this;
    }

}
