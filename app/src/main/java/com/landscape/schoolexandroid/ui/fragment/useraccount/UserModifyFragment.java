package com.landscape.schoolexandroid.ui.fragment.useraccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.useraccount.UserModifyView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 1 on 2016/7/7.
 */
public class UserModifyFragment extends BaseFragment implements UserModifyView<BasePresenter> {

    BtnClickListener btnClickListener;

    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_class)
    TextView tvClass;

    @OnClick(R.id.rl_passwd_modify)
    void passwd(View view){
        if (btnClickListener != null) {
            btnClickListener.passwd();
        }
    }

    @OnClick(R.id.logout)
    void logOut(View view){
        if (btnClickListener != null) {
            btnClickListener.quit();
        }
    }

    @Override
    public void refreshUserInfo(UserAccount userAccount) {
        tvName.setText(userAccount.getData().getName());
        tvClass.setText(userAccount.getData().getGradeName()+userAccount.getData().getTeamName());
        tvSex.setText(userAccount.getData().getGender());
    }

    @Override
    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_user_modify;
    }
}
