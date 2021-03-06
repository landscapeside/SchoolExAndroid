package com.landscape.schoolexandroid.views.home;

import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

import java.util.List;

import retrofit2.Call;

/**
 * Created by 1 on 2016/4/26.
 */
public interface MenuView<T extends BasePresenter> extends BaseView<T> {
    void loadUserAccount(UserAccount userAccount);
    void listData(List<MenuItemBean> listData);

    int getCurrentIdx();

    void setUserAccountListener(UserAccountListener userAccountListener);

    interface UserAccountListener{
        void userAccount();
    }


    interface OnMenuItemSelectListener{
        void onSelect(int position);
    }

    void setMenuItemSelectListener(OnMenuItemSelectListener listener);

    class MenuItemBean{
        public int drawResId;
        public String name;
    }

}
