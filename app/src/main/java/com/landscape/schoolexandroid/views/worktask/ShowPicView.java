package com.landscape.schoolexandroid.views.worktask;

import android.graphics.drawable.Drawable;

import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

/**
 * Created by landscape on 2016/7/9.
 */
public interface ShowPicView<T extends BasePresenter> extends BaseView<T> {
    void showThumb(Drawable drawable);
    void showPic(Drawable drawable);
}
