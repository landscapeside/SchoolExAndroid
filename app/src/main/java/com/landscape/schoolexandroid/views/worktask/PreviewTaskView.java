package com.landscape.schoolexandroid.views.worktask;

import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

/**
 * Created by 1 on 2016/6/27.
 */
public interface PreviewTaskView<T extends BasePresenter> extends BaseView<T> {

    void previewTask(String url);
    void startEnable(boolean isEnable);
    void setClickListener(ClickListener clickListener);

    interface ClickListener{
        void start();
    }
}
