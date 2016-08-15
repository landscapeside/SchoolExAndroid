package com.landscape.schoolexandroid.ui.fragment.home;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.adapter.home.WorkTaskAdapter;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.home.WorkTaskListView;
import com.utils.behavior.PopupWindowUtil;
import com.utils.behavior.ToastUtil;
import com.utils.datahelper.CollectionUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 1 on 2016/6/22.
 */
public class WorkTaskFragment extends BaseFragment implements WorkTaskListView<BasePresenter> {
    private WorkTaskAdapter workTaskAdapter;
    protected QuickAdapter subjectFilterAdapter,typeFilterAdapter,stateFilterAdapter;
    private OnListItemSelectListener onWorkItemSelectListener = null;
    protected View SubjectFilterContent, TypeFilterContent, StateFilterContent;
    protected OnFilterSelector onFilterSelector;

    @Bind(R.id.list_task)
    ListView listTask;
    @Bind(R.id.swipeRefreshLayout)
    PullRefreshLayout pullRefreshLayout;
    @Bind(R.id.tv_subject)
    TextView tvSubject;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.list_empty)
    View emptyView;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public void listWork(List<ExaminationTaskInfo> listData) {
        pullRefreshLayout.setRefreshing(false);
        if (CollectionUtils.isEmpty(listData)) {
            ToastUtil.show(getActivity(),"没有任务");
            emptyView.setVisibility(View.VISIBLE);
            pullRefreshLayout.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            pullRefreshLayout.setVisibility(View.VISIBLE);
            if (workTaskAdapter == null) {
                // TODO: 2016/6/22 更新数据
                workTaskAdapter = new WorkTaskAdapter(getActivity(),listData);
                listTask.setAdapter(workTaskAdapter);
                if (onWorkItemSelectListener != null) {
                    workTaskAdapter.setOnItemClickListener(info -> onWorkItemSelectListener.onSelect(info));
                }
            } else {
                workTaskAdapter.replaceAll(listData);
            }
        }
    }

    @Override
    public void subjectFilter(List<UserAccount.DataBean.SubjectTypeBean> subjectTypes) {
        if (SubjectFilterContent == null) {
            SubjectFilterContent = View.inflate(getActivity(),R.layout.view_filter_list, null);
        }
        if (subjectFilterAdapter == null) {
            subjectFilterAdapter = new QuickAdapter<UserAccount.DataBean.SubjectTypeBean>(getActivity(),R.layout.item_worktask,subjectTypes) {
                @Override
                protected void convert(BaseAdapterHelper helper, UserAccount.DataBean.SubjectTypeBean item) {
                    helper.setText(R.id.tv_name, item.getName());
                }
            };
            ListView listView = (ListView) SubjectFilterContent.findViewById(R.id.list_filter);
            listView.setAdapter(subjectFilterAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                PopupWindowUtil.dismiss();
                if (onFilterSelector != null) {
                    onFilterSelector.onSubjectSelect(subjectTypes.get(position));
                }
                tvSubject.setText(subjectTypes.get(position).getName());
            });
        } else {
            subjectFilterAdapter.replaceAll(subjectTypes);
        }
    }

    @Override
    public void typeFilter(List<UserAccount.DataBean.ExaminationPapersTypeBean> examPaperModes) {
        if (TypeFilterContent == null) {
            TypeFilterContent = View.inflate(getActivity(),R.layout.view_filter_list, null);
        }
        if (typeFilterAdapter == null) {
            typeFilterAdapter = new QuickAdapter<UserAccount.DataBean.ExaminationPapersTypeBean>(getActivity(),R.layout.item_worktask,examPaperModes) {
                @Override
                protected void convert(BaseAdapterHelper helper, UserAccount.DataBean.ExaminationPapersTypeBean item) {
                    helper.setText(R.id.tv_name, item.getName());
                }
            };
            ListView listView = (ListView) TypeFilterContent.findViewById(R.id.list_filter);
            listView.setAdapter(typeFilterAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                PopupWindowUtil.dismiss();
                if (onFilterSelector != null) {
                    onFilterSelector.onTypeSelect(examPaperModes.get(position));
                }
                tvType.setText(examPaperModes.get(position).getName());
            });
        } else {
            typeFilterAdapter.replaceAll(examPaperModes);
        }
    }

    @Override
    public void stateFilter(List<TaskStatus> taskStatusList) {
        if (StateFilterContent == null) {
            StateFilterContent = View.inflate(getActivity(),R.layout.view_filter_list, null);
        }
        if (stateFilterAdapter == null) {
            stateFilterAdapter = new QuickAdapter<TaskStatus>(getActivity(),R.layout.item_worktask,taskStatusList) {
                @Override
                protected void convert(BaseAdapterHelper helper, TaskStatus item) {
                    helper.setText(R.id.tv_name, item.getFilterName());
                }
            };
            ListView listView = (ListView) StateFilterContent.findViewById(R.id.list_filter);
            listView.setAdapter(stateFilterAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                PopupWindowUtil.dismiss();
                if (onFilterSelector != null) {
                    onFilterSelector.onStateSelect(taskStatusList.get(position));
                }
                tvState.setText(taskStatusList.get(position).getFilterName());
            });
        } else {
            stateFilterAdapter.replaceAll(taskStatusList);
        }
    }

    @Override
    public void setSubjectEnable(boolean enable) {
        tvSubject.setVisibility(enable?View.VISIBLE:View.GONE);
    }

    @Override
    public void setTypeEnable(boolean enable) {
        tvType.setVisibility(enable?View.VISIBLE:View.GONE);
    }

    @Override
    public void setStateEnable(boolean enable) {
        tvState.setVisibility(enable?View.VISIBLE:View.GONE);
    }

    @Override
    public void setOnFilterSelector(OnFilterSelector onFilterSelector) {
        this.onFilterSelector = onFilterSelector;
    }

    @OnClick(R.id.tv_subject)
    void subjectFilterClick(View view){
        PopupWindowUtil.showPopupWindow(getActivity(),tvSubject,SubjectFilterContent);
    }

    @OnClick(R.id.tv_type)
    void typeFilterClick(View view){
        PopupWindowUtil.showPopupWindow(getActivity(),tvType,TypeFilterContent);
    }

    @OnClick(R.id.tv_state)
    void stateFilterClick(View view){
        PopupWindowUtil.showPopupWindow(getActivity(),tvState,StateFilterContent);
    }

    @Override
    public void startRefresh() {
        listTask.setSelectionAfterHeaderView();
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
        onWorkItemSelectListener = listener;
    }

    @Override
    public int getResId() {
        return R.layout.view_task_list;
    }
}
