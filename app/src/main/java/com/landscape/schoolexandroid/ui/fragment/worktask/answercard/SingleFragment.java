package com.landscape.schoolexandroid.ui.fragment.worktask.answercard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.mode.worktask.AlternativeContent;
import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.StudentAnswer;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.answercard.SingleView;
import com.orhanobut.logger.Logger;
import com.utils.system.ScreenParam;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 1 on 2016/7/18.
 */
public class SingleFragment extends BaseFragment implements SingleView<BasePresenter> {
    DataChangeListener changeListener;
    StudentAnswer studentAnswer;

    @Bind(R.id.radio_group)
    RadioGroup group;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.item_answer_single_pager;
    }

    @Override
    public void build(AnswerType type, List<AlternativeContent> alternativeContents, StudentAnswer studentAnswer) {
        this.studentAnswer = studentAnswer;
        group.removeAllViews();
        int radioMargin = ((ScreenParam.screenWidth-ScreenParam.dp2px(getActivity(), 20))/alternativeContents.size()-ScreenParam.dp2px(getActivity(), 30))/2;
        for (int i = 0; i < alternativeContents.size(); i++) {
            RadioButton radioButton = (RadioButton) View.inflate(getActivity(), R.layout.view_radio_button, null);
            radioButton.setText(alternativeContents.get(i).Id);
            group.addView(radioButton);
            RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) radioButton.getLayoutParams();
            layoutParams.setMargins(radioMargin, radioMargin, radioMargin, radioMargin);
            layoutParams.width = ScreenParam.dp2px(getActivity(), 30);
            layoutParams.height = ScreenParam.dp2px(getActivity(), 30);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && changeListener!= null) {
                    if (this.studentAnswer == null) {
                        this.studentAnswer = new StudentAnswer();
                        this.studentAnswer.Id = type.getId();
                        this.studentAnswer.TypeId = type.getTypeId();
                    }
                    this.studentAnswer.Answer = radioButton.getText().toString();
                    changeListener.onDataChanged(this.studentAnswer);
                }
            });
        }
        if (studentAnswer != null) {
            for (int i = 0; i < alternativeContents.size(); i++) {
                if (alternativeContents.get(i).Id.equals(studentAnswer.Answer)) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    radioButton.setChecked(true);
                    break;
                }
            }
        }
    }

    @Override
    public void setDataChangeListener(DataChangeListener listener) {
        changeListener = listener;
    }
}
