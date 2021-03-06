package com.landscape.schoolexandroid.ui.fragment.worktask.answercard;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.StudentAnswer;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.answercard.DecideView;
import com.utils.system.ScreenParam;

import butterknife.Bind;

/**
 * Created by 1 on 2016/7/18.
 */
public class DecideFragment extends BaseFragment implements DecideView<BasePresenter> {

    DataChangeListener changeListener;

    StudentAnswer studentAnswer;

    @Bind(R.id.radio_def)
    RadioButton radioDef;
    @Bind(R.id.radio_right)
    RadioButton radioRight;
    @Bind(R.id.radio_err)
    RadioButton radioErr;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.item_answer_decide_pager;
    }

    @Override
    public void build(AnswerType type, StudentAnswer studentanswer) {
        studentAnswer = studentanswer;

        radioDef.setChecked(true);
//        int radioMargin = (ScreenParam.screenWidth/2-ScreenParam.dp2px(getActivity(), 50))/2;
//        RadioGroup.LayoutParams rightLayoutParams = (RadioGroup.LayoutParams) radioRight.getLayoutParams();
//        rightLayoutParams.setMargins(radioMargin, 0, radioMargin, 0);
//        radioRight.setLayoutParams(rightLayoutParams);
//
//        RadioGroup.LayoutParams errLayoutParams = (RadioGroup.LayoutParams) radioErr.getLayoutParams();
//        errLayoutParams.setMargins(radioMargin, 0, radioMargin, 0);
//        radioErr.setLayoutParams(errLayoutParams);

        radioRight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && changeListener != null) {
                if (studentAnswer == null) {
                    studentAnswer = new StudentAnswer();
                    studentAnswer.Id = type.getId();
                    studentAnswer.TypeId = type.getTypeId();
                }
                studentAnswer.Answer = "T";
                changeListener.onDataChanged(studentAnswer);
            }
        });
        radioErr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && changeListener != null) {
                if (studentAnswer == null) {
                    studentAnswer = new StudentAnswer();
                    studentAnswer.Id = type.getId();
                    studentAnswer.TypeId = type.getTypeId();
                }
                studentAnswer.Answer = "F";
                changeListener.onDataChanged(studentAnswer);
            }
        });
        if (studentAnswer != null) {
            if (studentAnswer.Answer.equals("T")) {
                radioRight.setChecked(true);
            } else {
                radioErr.setChecked(true);
            }
        }
    }

    @Override
    public void setDataChangeListener(DataChangeListener listener) {
        changeListener = listener;
    }
}
