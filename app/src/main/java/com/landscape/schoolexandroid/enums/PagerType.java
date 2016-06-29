package com.landscape.schoolexandroid.enums;

import com.landscape.schoolexandroid.presenter.worktask.PreviewTaskPresenterImpl;

/**
 * Created by 1 on 2016/6/27.
 */
public enum PagerType {

    PREVIEW_TASK(0,PreviewTaskPresenterImpl.class),


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
            default:
                return NONE;
        }
    }

}
