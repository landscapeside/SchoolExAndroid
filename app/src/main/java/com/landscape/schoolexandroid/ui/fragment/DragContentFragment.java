package com.landscape.schoolexandroid.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsware.draglayout.DragCallBack;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.home.DragContentView;
import com.nineoldandroids.view.ViewHelper;
import com.utils.behavior.ActivityUtils;

import butterknife.Bind;

/**
 * Created by 1 on 2016/4/26.
 */
public class DragContentFragment extends BaseFragment implements DragContentView<BasePresenter>, DragCallBack {
    AvatarClickListener avatarClickListener;
    Fragment current;

    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_main_content;
    }

    public void shake() {
        ivIcon.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {
        shake();
    }

    @Override
    public void onDrag(float percent) {
        ViewHelper.setAlpha(ivIcon, 1 - percent);
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setContentFragment(Fragment fragment) {
        if (current == null) {
            ActivityUtils.addFragmentToActivity(getChildFragmentManager(), fragment, R.id.contentFrame);
        } else if(current != fragment){
            ActivityUtils.showFragmentToActivity(getChildFragmentManager(),current,fragment,R.id.contentFrame);
        }
        current = fragment;
    }

    @Override
    public void setAvatarClick(AvatarClickListener avatarClick) {
        avatarClickListener = avatarClick;
        if (avatarClickListener != null) {
            ivIcon.setOnClickListener(v -> avatarClickListener.avatarClick());
        }
    }
}
