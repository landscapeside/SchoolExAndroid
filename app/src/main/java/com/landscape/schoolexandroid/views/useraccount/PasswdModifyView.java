package com.landscape.schoolexandroid.views.useraccount;

import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

/**
 * Created by 1 on 2016/7/7.
 */
public interface PasswdModifyView<T extends BasePresenter> extends BaseView<T>{

    void setBtnClickListener(BtnClickListener btnClickListener);

    interface BtnClickListener{
        void modify(String oldPwd,String newPwd);
    }
}
