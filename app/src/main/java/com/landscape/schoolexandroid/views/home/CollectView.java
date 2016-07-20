package com.landscape.schoolexandroid.views.home;

import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

import java.util.List;

/**
 * Created by 1 on 2016/7/19.
 */
public interface CollectView<T extends BasePresenter> extends BaseView<T> {
    void previewTask(String url);

    void subjectFilter(List<UserAccount.DataBean.SubjectTypeBean> subjectTypes);

    interface OnFilterSelector{
        void onSubjectSelect(UserAccount.DataBean.SubjectTypeBean subjectType);
    }
    void setOnFilterSelector(OnFilterSelector onFilterSelector);

}
