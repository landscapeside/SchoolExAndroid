package com.landscape.schoolexandroid.views.home;

import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

/**
 * Created by 1 on 2016/4/26.
 */
public interface MenuView<T extends BasePresenter> extends BaseView<T> {

    void listData(String[] listData);

    interface OnMenuItemSelectListener{
        void onSelect(int position);
    }

    void setMenuItemSelectListener(OnMenuItemSelectListener listener);


}
