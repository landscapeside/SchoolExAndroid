package com.landscape.schoolexandroid.views.home;

import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

import java.util.List;

/**
 * Created by 1 on 2016/6/22.
 */
public interface WorkTaskListView<T extends BasePresenter> extends BaseView<T> {

    void listData(List<ExaminationTaskInfo> listData);

    void startRefresh();
    void cancelRefresh();

    void setRefreshListener(RefreshListener refreshListener);
    interface RefreshListener{
        void refreshData();
    }

    interface OnListItemSelectListener{
        void onSelect(int position);
    }

    void setListItemSelectListener(OnListItemSelectListener listener);

}
