package com.landscape.schoolexandroid.ui.fragment.useraccount;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.useraccount.FeedBkView;
import com.utils.behavior.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 1 on 2016/7/11.
 */
public class FeedbkFragment extends BaseFragment implements FeedBkView<BasePresenter> {
    BtnClickListener btnClickListener = null;

    @Bind(R.id.type_question)
    RadioButton typeQuestion;
    @Bind(R.id.type_advise)
    RadioButton typeAdvise;
    @Bind(R.id.type_other)
    RadioButton typeOther;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.edit_content)
    EditText editContent;

    @Override
    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    @OnClick(R.id.confirm)
    void submit(View view){
        if (btnClickListener != null && check()) {
            String type = "";
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.type_question:
                    type = "问题";
                    break;
                case R.id.type_advise:
                    type = "建议";
                    break;
                case R.id.type_other:
                    type = "其他";
                    break;
            }
            btnClickListener.submit(editContent.getText().toString().trim(),type);
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(editContent.getText().toString().trim())) {
            ToastUtil.show(getActivity(),"请输入反馈内容");
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_feedbk;
    }
}
