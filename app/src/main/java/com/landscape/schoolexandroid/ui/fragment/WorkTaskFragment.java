package com.landscape.schoolexandroid.ui.fragment;

import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.home.WorkTaskListView;

import butterknife.Bind;

/**
 * Created by 1 on 2016/6/22.
 */
public class WorkTaskFragment extends BaseFragment implements WorkTaskListView<BasePresenter> {
    RefreshListener refreshListener;
    QuickAdapter adapter;

    @Bind(R.id.list_task)
    ListView listTask;
    @Bind(R.id.swipeRefreshLayout)
    PullRefreshLayout pullRefreshLayout;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public void listData(String[] listData) {
        pullRefreshLayout.setRefreshing(false);
        if (adapter == null) {
            // TODO: 2016/6/22 更新数据
        } else {

        }

    }

    @Override
    public void setRefreshListener(RefreshListener refreshListener) {
        if (refreshListener != null) {
            pullRefreshLayout.setOnRefreshListener(() -> refreshListener.refreshData());
        }
    }

    @Override
    public void setListItemSelectListener(OnListItemSelectListener listener) {

    }

    @Override
    public int getResId() {
        return R.layout.view_task_list;
    }
}
