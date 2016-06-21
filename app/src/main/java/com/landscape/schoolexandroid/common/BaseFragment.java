package com.landscape.schoolexandroid.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

import butterknife.ButterKnife;

/**
 * Created by 1 on 2016/4/26.
 */
public abstract class  BaseFragment extends Fragment implements BaseView<BasePresenter> {
    protected BaseView.ViewLifeListener lifeListener;
    protected BasePresenter presenter;
    public abstract int getLayoutResId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutResId(), container, false);
        return root;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLifeListener(ViewLifeListener lifeListener) {
        this.lifeListener = lifeListener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        if (lifeListener != null) {
            lifeListener.onInitialized();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (lifeListener != null) {
            lifeListener.onDestroy();
        }
    }
}
