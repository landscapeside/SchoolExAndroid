package com.landscape.schoolexandroid.ui.fragment.useraccount;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.useraccount.PasswdModifyView;
import com.utils.behavior.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 1 on 2016/7/7.
 */
public class PwdModifyFragment extends BaseFragment implements PasswdModifyView<BasePresenter> {
    BtnClickListener btnClickListener;

    @Bind(R.id.edit_old_pwd)
    EditText editOldPwd;
    @Bind(R.id.edit_new_pwd)
    EditText editNewPwd;
    @Bind(R.id.edit_confirm_pwd)
    EditText editConfirmPwd;

    @OnClick(R.id.confirm)
    void confirm(View view){
        if (check() && btnClickListener != null) {
            btnClickListener.modify(editOldPwd.getText().toString().trim(),editNewPwd.getText().toString().trim());
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(editOldPwd.getText().toString().trim())) {
            ToastUtil.show(getActivity(),"旧密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(editNewPwd.getText().toString().trim())) {
            ToastUtil.show(getActivity(),"新密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(editConfirmPwd.getText().toString().trim())) {
            ToastUtil.show(getActivity(),"确认密码不能为空");
            return false;
        }
        if (TextUtils.equals(editNewPwd.getText().toString(), editConfirmPwd.getText().toString())) {
            ToastUtil.show(getActivity(),"新密码与确认密码不一致");
            return false;
        }
        return true;
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
        return R.layout.view_passwd_modify;
    }
}
