package com.landscape.schoolexandroid.ui.fragment.worktask;

import android.text.TextUtils;
import android.widget.Button;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseWebFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.PreviewTaskView;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

/**
 * Created by 1 on 2016/6/27.
 */
public class PreviewTaskFragment extends BaseWebFragment implements PreviewTaskView<BasePresenter> {

    String url = "";

    @Bind(R.id.startTask)
    Button startTask;

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
    public void setClickListener(ClickListener clickListener) {
        startTask.setOnClickListener(v -> clickListener.start());
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
