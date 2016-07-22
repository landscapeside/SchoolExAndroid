package com.landscape.schoolexandroid.ui.fragment.worktask.answercard;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.StudentAnswer;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.answercard.EditSimpleView;
import com.landscape.schoolexandroid.views.worktask.answercard.MultiView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;

/**
 * Created by 1 on 2016/7/18.
 */
public class EditSimpleFragment extends BaseFragment implements EditSimpleView<BasePresenter> {
    DataChangeListener changeListener;
    StudentAnswer studentAnswer;
    AnswerType type = null;
    EditTextWatcher textWatcher = null;
    boolean initFlag = false;

    @Bind(R.id.ll_pics)
    View llPics;
    @Bind(R.id.edit_content)
    EditText editContent;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.item_answer_edit_pager;
    }

    @Override
    public void build(AnswerType type, StudentAnswer studentanswer) {
        studentAnswer = studentanswer;
        llPics.setVisibility(View.GONE);
        this.type = type;
        if (textWatcher != null) {
            editContent.removeTextChangedListener(textWatcher);
        }
        textWatcher = new EditTextWatcher();
        editContent.addTextChangedListener(textWatcher);
        initFlag = true;
        editContent.setText("");
    }

    @Override
    public void hideSoftKeyBord() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editContent.getWindowToken(), 0); //强制隐藏键盘
    }

    @Override
    public void setDataChangeListener(DataChangeListener listener) {
        changeListener = listener;
    }

    class EditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editContent.removeTextChangedListener(this);
            if (studentAnswer == null) {
                studentAnswer = new StudentAnswer();
                studentAnswer.Id = type.getId();
                studentAnswer.TypeId = type.getTypeId();
            }
            if (!initFlag) {
                studentAnswer.Answer = editContent.getText().toString();
                if (changeListener != null) {
                    changeListener.onDataChanged(studentAnswer);
                }
            } else {
                /**
                 * 设置默认值
                 */
                if (!TextUtils.isEmpty(studentAnswer.Answer)) {
                    editContent.setText(studentAnswer.Answer);
                }
                initFlag = false;
            }
            editContent.addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
