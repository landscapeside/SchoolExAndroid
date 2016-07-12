package com.landscape.schoolexandroid.views.home;

import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

import java.util.List;

/**
 * Created by 1 on 2016/7/12.
 */
public interface BaseListView<T extends BasePresenter> extends BaseView<T> {
    void subjectFilter(List<UserAccount.DataBean.SubjectTypeBean> subjectTypes);
    void typeFilter(List<UserAccount.DataBean.ExaminationPapersTypeBean> examPaperModes);
    void stateFilter(List<TaskStatus> taskStatusList);

    interface OnFilterSelector{
        void onSubjectSelect(UserAccount.DataBean.SubjectTypeBean subjectType);
        void onTypeSelect(UserAccount.DataBean.ExaminationPapersTypeBean paperMode);
        void onStateSelect(TaskStatus status);
    }
    void setOnFilterSelector(OnFilterSelector onFilterSelector);

    void startRefresh();
    void cancelRefresh();

    void setRefreshListener(RefreshListener refreshListener);
    interface RefreshListener{
        void refreshData();
    }
}
