package com.landscape.schoolexandroid.ui.fragment.home;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.adapter.home.WorkTaskAdapter;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.home.WorkTaskListView;
import com.utils.behavior.ToastUtil;
import com.utils.datahelper.CollectionUtils;

import java.util.List;

import butterknife.Bind;

/**
 * Created by 1 on 2016/6/22.
 */
public class WorkTaskFragment extends BaseFragment implements WorkTaskListView<BasePresenter> {
    RefreshListener refreshListener;
    WorkTaskAdapter adapter;

    @Bind(R.id.list_task)
    ListView listTask;
    @Bind(R.id.swipeRefreshLayout)
    PullRefreshLayout pullRefreshLayout;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public void listData(List<ExaminationTaskInfo> listData) {
        pullRefreshLayout.setRefreshing(false);
        if (CollectionUtils.isEmpty(listData)) {
            ToastUtil.show(getActivity(),"没有任务");
        } else {
            if (adapter == null) {
                // TODO: 2016/6/22 更新数据
                adapter = new WorkTaskAdapter(getActivity(),listData);
                listTask.setAdapter(adapter);
//                adapter = new QuickAdapter<ExaminationTaskInfo>(getActivity(),R.layout.item_worktask,listData) {
//                    @Override
//                    protected void convert(BaseAdapterHelper helper, ExaminationTaskInfo item) {
//                        helper.setText(R.id.tv_name, item.getName());
//                    }
//                };
                listTask.setAdapter(adapter);
            } else {
                adapter.replaceAll(listData);
            }
        }
    }

    @Override
    public void startRefresh() {
        pullRefreshLayout.setRefreshing(true);
    }

    @Override
    public void cancelRefresh() {
        pullRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setRefreshListener(RefreshListener refreshListener) {
        if (refreshListener != null) {
            pullRefreshLayout.setOnRefreshListener(() -> refreshListener.refreshData());
        }
    }

    @Override
    public void setListItemSelectListener(OnListItemSelectListener listener) {
        listTask.setOnItemClickListener((parent, view, position, id) -> {
            listener.onSelect(position);
        });
    }

    @Override
    public int getResId() {
        return R.layout.view_task_list;
    }
}
