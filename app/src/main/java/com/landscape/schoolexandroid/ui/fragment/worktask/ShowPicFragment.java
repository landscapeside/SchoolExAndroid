package com.landscape.schoolexandroid.ui.fragment.worktask;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.ShowPicView;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by landscape on 2016/7/9.
 */
public class ShowPicFragment extends BaseFragment implements ShowPicView<BasePresenter> {

    private PhotoViewAttacher mAttacher;

    @Bind(R.id.iv_photo)
    PhotoView ivPhoto;

    @Override
    public void showThumb(Drawable drawable) {
        if (mAttacher != null) {
            mAttacher.cleanup();
        }
        if (drawable != null) {
            ivPhoto.setImageDrawable(drawable);
        }
        mAttacher = new PhotoViewAttacher(ivPhoto);
    }

    @Override
    public void showPic(Drawable drawable) {
        if (mAttacher != null) {
            mAttacher.cleanup();
        }
        if (drawable != null) {
            ivPhoto.setImageDrawable(drawable);
        }
        mAttacher = new PhotoViewAttacher(ivPhoto);
    }

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_show_pic;
    }
}
