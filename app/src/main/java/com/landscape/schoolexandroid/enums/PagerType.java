package com.landscape.schoolexandroid.enums;

import com.landscape.schoolexandroid.presenter.worktask.AnswerPresenterImpl;
import com.landscape.schoolexandroid.presenter.worktask.PreviewTaskPresenterImpl;
import com.landscape.schoolexandroid.presenter.worktask.QuestionLocationPresenterImpl;

/**
 * Created by 1 on 2016/6/27.
 */
public enum PagerType {

    PREVIEW_TASK(0,PreviewTaskPresenterImpl.class),

    ANSWER(2, AnswerPresenterImpl.class),

    QUESTION_LOCATION(3, QuestionLocationPresenterImpl.class),


    NONE(-1,Object.class);


    private Class typeCls;
    private int type;

    PagerType(int type,Class typeCls) {
        this.typeCls = typeCls;
        this.type = type;
    }

    public Class getTypeCls() {
        return typeCls;
    }

    public int getType() {
        return type;
    }

    public static PagerType getType(int type) {
        switch (type) {
            case 0:
                return PREVIEW_TASK;
            case 2:
                return ANSWER;
            case 3:
                return QUESTION_LOCATION;
            default:
                return NONE;
        }
    }

}
