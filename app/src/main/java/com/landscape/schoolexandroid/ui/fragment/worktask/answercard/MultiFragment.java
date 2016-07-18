package com.landscape.schoolexandroid.ui.fragment.worktask.answercard;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.mode.worktask.AlternativeContent;
import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.StudentAnswer;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.answercard.MultiView;
import com.landscape.schoolexandroid.views.worktask.answercard.SingleView;
import com.utils.system.ScreenParam;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by 1 on 2016/7/18.
 */
public class MultiFragment extends BaseFragment implements MultiView<BasePresenter> {
    DataChangeListener changeListener;
    StudentAnswer studentAnswer;

    @Bind(R.id.multi_content)
    LinearLayout multiContent;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.item_answer_multi;
    }

    @Override
    public void build(AnswerType type, List<AlternativeContent> alternativeContents, StudentAnswer studentAnswer) {
        this.studentAnswer = studentAnswer;
        multiContent.removeAllViews();
        for (int i = 0; i < alternativeContents.size(); i++) {
            TextView radioButton = (TextView) View.inflate(getActivity(), R.layout.view_multi_button, null);
            radioButton.setText(alternativeContents.get(i).Id);
            radioButton.setOnClickListener(v -> {
                v.setSelected(!v.isSelected());
                if (this.studentAnswer == null) {
                    this.studentAnswer = new StudentAnswer();
                    this.studentAnswer.Id = type.getId();
                    this.studentAnswer.TypeId = type.getTypeId();
                }
                if (v.isSelected()) {
                    if (TextUtils.isEmpty(this.studentAnswer.Answer)) {
                        this.studentAnswer.Answer = radioButton.getText().toString();
                    } else {
                        String[] answers = this.studentAnswer.Answer.split(",");
                        String[] newAnswers = new String[answers.length + 1];
                        System.arraycopy(answers, 0, newAnswers, 0, answers.length);
                        newAnswers[newAnswers.length - 1] = radioButton.getText().toString();
                        ArrayList<String> answerList = new ArrayList<>(Arrays.asList(newAnswers));
                        Collections.sort(answerList);
                        newAnswers = answerList.toArray(new String[0]);
                        this.studentAnswer.Answer = StringUtils.join(newAnswers, ",");
                    }
                } else {
                    if (!TextUtils.isEmpty(this.studentAnswer.Answer)) {
                        String[] answers = this.studentAnswer.Answer.split(",");
                        ArrayList<String> answerList = new ArrayList<>(Arrays.asList(answers));
                        for (int i1 = 0; i1 < answerList.size(); i1++) {
                            if (answerList.get(i1).equals(radioButton.getText().toString())) {
                                answerList.remove(i1);
                                break;
                            }
                        }
                        String[] newAnswers = answerList.toArray(new String[0]);
                        this.studentAnswer.Answer = StringUtils.join(newAnswers, ",");
                    }
                }
                if (changeListener != null) {
                    changeListener.onDataChanged(this.studentAnswer);
                }
            });
            multiContent.addView(radioButton);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) radioButton.getLayoutParams();
            layoutParams.setMargins(20, 20, 20, 20);
            layoutParams.width = ScreenParam.dp2px(getActivity(), 30);
            layoutParams.height = ScreenParam.dp2px(getActivity(), 30);
            radioButton.setLayoutParams(layoutParams);
        }
        if (studentAnswer != null) {
            for (int i = 0; i < alternativeContents.size(); i++) {
                if (studentAnswer.Answer.contains(alternativeContents.get(i).Id)) {
                    TextView radioButton = (TextView) multiContent.getChildAt(i);
                    radioButton.setSelected(true);
                }
            }
        }


    }

    @Override
    public void setDataChangeListener(DataChangeListener listener) {
        changeListener = listener;
    }
}
