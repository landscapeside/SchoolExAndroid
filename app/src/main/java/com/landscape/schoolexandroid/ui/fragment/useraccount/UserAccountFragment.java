package com.landscape.schoolexandroid.ui.fragment.useraccount;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.enums.SexType;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.landscape.schoolexandroid.views.useraccount.UserAccountView;
import com.landscape.weight.CircleImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 1 on 2016/7/7.
 */
public class UserAccountFragment extends BaseFragment implements UserAccountView<BasePresenter>{

    BtnClickListener btnClickListener = null;

    @Bind(R.id.icon_avatar)
    CircleImageView iconAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.tv_lv_small)
    TextView tvLvSmall;
    @Bind(R.id.tv_lv_normal)
    TextView tvLvNormal;
    @Bind(R.id.tv_lv_big)
    TextView tvLvBig;

    @OnClick(R.id.rl_user_modify)
    void userModify(View view){
        if (btnClickListener != null) {
            btnClickListener.userModity();
        }
    }

    @OnClick(R.id.rl_flower_chart)
    void flowerChart(View view){
        if (btnClickListener != null) {
            btnClickListener.flowerChart();
        }
    }

    @OnClick(R.id.rl_wonder_chart)
    void wonderChart(View view){
        if (btnClickListener != null) {
            btnClickListener.wonderChart();
        }
    }

    @OnClick(R.id.rl_avatar)
    void avatar(View view){
        if (btnClickListener != null) {
            PhotoHelper.subcriberView = iconAvatar;
            btnClickListener.avatar();
        }
    }

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_user_account;
    }

    @Override
    public void refreshUserInfo(UserAccount userAccount) {
        if (!TextUtils.isEmpty(userAccount.getData().getPhoto())) {
            Picasso.with(getActivity()).load(userAccount.getData().getPhoto()).into(iconAvatar);
        } else {
            iconAvatar.setImageResource(R.drawable.icon_def_avatar);
        }
        tvName.setText(userAccount.getData().getName());
        tvClass.setText(userAccount.getData().getGradeName()+userAccount.getData().getTeamName());
        Drawable sexDrawable = getResources().getDrawable(
                SexType.MALE == SexType.getSex(userAccount.getData().getGender()) ? R.drawable.icon_male : R.drawable.icon_female);
        sexDrawable.setBounds(0,0,sexDrawable.getMinimumWidth(),sexDrawable.getMinimumHeight());
        tvClass.setCompoundDrawables(sexDrawable,null,null,null);
    }

    @Override
    public void setLvSmall(int lvSmall) {
        tvLvSmall.setText(""+lvSmall);
    }

    @Override
    public void setLvNormal(int lvNormal) {
        tvLvNormal.setText(""+lvNormal);
    }

    @Override
    public void setLvBig(int lvBig) {
        tvLvBig.setText(""+lvBig);
    }

    @Override
    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }
}
