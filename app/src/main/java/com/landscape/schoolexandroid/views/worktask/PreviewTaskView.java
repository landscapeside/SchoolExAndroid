package com.landscape.schoolexandroid.views.worktask;

import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.weight.FlingRelativeLayout;
import com.landscape.weight.ScrollWebView;

import java.util.List;

/**
 * Created by 1 on 2016/6/27.
 */
public interface PreviewTaskView<T extends BasePresenter> extends BaseView<T> {

    void previewTask(String url);
    void startEnable(boolean isEnable);
    void setClickListener(ClickListener clickListener);
    void setFilterEnable(boolean enable);
    void subjectFilter(List<UserAccount.DataBean.SubjectTypeBean> subjectTypes);

    interface OnFilterSelector{
        void onSubjectSelect(UserAccount.DataBean.SubjectTypeBean subjectType);
    }
    void setOnFilterSelector(OnFilterSelector onFilterSelector);

    interface ClickListener{
        void start();
    }

    void setDragListener(ScrollWebView.DragHorizontalListener dragListener);
}
