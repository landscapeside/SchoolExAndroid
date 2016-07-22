package com.landscape.schoolexandroid.views.worktask.answercard;

import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.StudentAnswer;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

import java.util.Map;

/**
 * Created by 1 on 2016/7/18.
 */
public interface EditRichView<T extends BasePresenter> extends BaseView<T> {
    void build(AnswerType type, StudentAnswer studentAnswer, Map<String,String> localeImageMap);
    void hideSoftKeyBord();

    void setDataChangeListener(DataChangeListener listener);
    interface DataChangeListener{
        void onDataChanged(StudentAnswer studentAnswer);
    }
}
