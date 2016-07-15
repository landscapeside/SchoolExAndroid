package com.landscape.schoolexandroid.ui.fragment.worktask;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseWebFragment;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.PreviewTaskView;
import com.landscape.weight.FlingRelativeLayout;
import com.orhanobut.logger.Logger;
import com.utils.behavior.PopupWindowUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 1 on 2016/6/27.
 */
public class PreviewTaskFragment extends BaseWebFragment implements PreviewTaskView<BasePresenter> {

    String url = "";

    protected View SubjectFilterContent;
    protected QuickAdapter subjectFilterAdapter;
    protected OnFilterSelector onFilterSelector;

    @Bind(R.id.startTask)
    Button startTask;
    @Bind(R.id.ll_filter)
    View filterContent;
    @Bind(R.id.tv_subject)
    TextView tvSubject;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public void previewTask(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("H5地址不能为空");
        }
        Logger.i(url);
        this.url = url;
        mWebView.loadUrl(url);
    }

    @Override
    public void startEnable(boolean isEnable) {
        startTask.setVisibility(isEnable? View.VISIBLE:View.GONE);
    }

    @Override
    public void setClickListener(ClickListener clickListener) {
        startTask.setOnClickListener(v -> clickListener.start());
    }

    @Override
    public void setFilterEnable(boolean enable) {
        filterContent.setVisibility(enable?View.VISIBLE:View.GONE);
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
    public void setOnFilterSelector(OnFilterSelector onFilterSelector) {
        this.onFilterSelector = onFilterSelector;
    }

    @OnClick(R.id.tv_subject)
    void subjectFilterClick(View view){
        PopupWindowUtil.showPopupWindow(getActivity(),tvSubject,SubjectFilterContent);
    }

    @Override
    public void setFlingListener(FlingRelativeLayout.FlingListener flingListener) {
        webViewLayout.setFlingListener(flingListener);
    }

    @Override
    public int getResId() {
        return R.layout.view_preview_task;
    }

    @Override
    public void refresh() {
        previewTask(url);
    }
}
