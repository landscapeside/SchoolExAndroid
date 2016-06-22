package com.landscape.schoolexandroid.views.home;


import android.support.v4.app.Fragment;

import com.jsware.draglayout.DragCallBack;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

/**
 * Created by 1 on 2016/4/26.
 */
public interface DragContentView<T extends BasePresenter> extends BaseView<T>,DragCallBack {

    void setTitle(String title);
    void setContentFragment(Fragment fragment);

    void setAvatarClick(AvatarClickListener avatarClick);
    interface AvatarClickListener{
        void avatarClick();
    }

}
