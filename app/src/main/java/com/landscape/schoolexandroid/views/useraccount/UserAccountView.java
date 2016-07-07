package com.landscape.schoolexandroid.views.useraccount;

import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

/**
 * Created by 1 on 2016/7/7.
 */
public interface UserAccountView<T extends BasePresenter> extends BaseView<T> {

    void refreshUserInfo(UserAccount userAccount);

    void setLvSmall(int lvSmall);

    void setLvNormal(int lvNormal);

    void setLvBig(int lvBig);

    void setBtnClickListener(BtnClickListener btnClickListener);

    interface BtnClickListener{
        void avatar();
        void userModity();
        void flowerChart();
        void wonderChart();
    }


}
