package com.landscape.schoolexandroid.ui.fragment.home;

import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.adapter.home.MistakeAdapter;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.mode.mistake.MistakeInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.home.MistakeListView;
import com.utils.behavior.ToastUtil;
import com.utils.datahelper.CollectionUtils;

import java.util.List;

import butterknife.Bind;

/**
 * Created by 1 on 2016/6/22.
 */
public class MistakeFragment extends WorkTaskFragment implements MistakeListView<BasePresenter> {
    MistakeAdapter mistakeAdapter;
    MistakeListView.OnListItemSelectListener onMistakeItemSelectListener = null;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public void listMistake(List<MistakeInfo> listData) {
        pullRefreshLayout.setRefreshing(false);
        if (CollectionUtils.isEmpty(listData)) {
            ToastUtil.show(getActivity(),"没有错题");
        } else {
            if (mistakeAdapter == null) {
                // TODO: 2016/6/22 更新数据
                mistakeAdapter = new MistakeAdapter(getActivity(),listData);
                listTask.setAdapter(mistakeAdapter);
                if (onMistakeItemSelectListener != null) {
                    mistakeAdapter.setOnItemClickListener(info -> onMistakeItemSelectListener.onSelect(info));
                }
            } else {
                mistakeAdapter.replaceAll(listData);
            }
        }
    }

    @Override
    public void setListItemSelectListener(MistakeListView.OnListItemSelectListener listener) {
        onMistakeItemSelectListener = listener;
    }

    @Override
    public int getResId() {
        return R.layout.view_task_list;
    }
}
