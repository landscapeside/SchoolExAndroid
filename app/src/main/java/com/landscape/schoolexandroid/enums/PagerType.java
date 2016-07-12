package com.landscape.schoolexandroid.enums;

import com.landscape.schoolexandroid.presenter.mistake.PreviewMistakePresenterImpl;
import com.landscape.schoolexandroid.presenter.useraccount.ChartPresenterImpl;
import com.landscape.schoolexandroid.presenter.useraccount.FeedBkPresenterImpl;
import com.landscape.schoolexandroid.presenter.useraccount.PasswdModifyPresenterImpl;
import com.landscape.schoolexandroid.presenter.useraccount.UserAccountPresenterImpl;
import com.landscape.schoolexandroid.presenter.useraccount.UserModifyPresenterImpl;
import com.landscape.schoolexandroid.presenter.worktask.AnswerPresenterImpl;
import com.landscape.schoolexandroid.presenter.worktask.PreviewTaskPresenterImpl;
import com.landscape.schoolexandroid.presenter.worktask.QuestionLocationPresenterImpl;
import com.landscape.schoolexandroid.presenter.worktask.ShowPicPresenterImpl;

/**
 * Created by 1 on 2016/6/27.
 */
public enum PagerType {

    PREVIEW_TASK(0,PreviewTaskPresenterImpl.class),

    ANSWER(2, AnswerPresenterImpl.class),

    QUESTION_LOCATION(3, QuestionLocationPresenterImpl.class),

    USER_CENTER(4, UserAccountPresenterImpl.class),

    USER_MODIFY(5, UserModifyPresenterImpl.class),

    PWD_MODIFY(6, PasswdModifyPresenterImpl.class),

    CHART(7, ChartPresenterImpl.class),

    SHOW_PIC(8, ShowPicPresenterImpl.class),

    FEEDBK(9, FeedBkPresenterImpl.class),

    PREVIEW_MISTAKE(10, PreviewMistakePresenterImpl.class),

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
            case 4:
                return USER_CENTER;
            case 5:
                return USER_MODIFY;
            case 6:
                return PWD_MODIFY;
            case 7:
                return CHART;
            case 8:
                return SHOW_PIC;
            case 9:
                return FEEDBK;
            case 10:
                return PREVIEW_MISTAKE;
            default:
                return NONE;
        }
    }

}
