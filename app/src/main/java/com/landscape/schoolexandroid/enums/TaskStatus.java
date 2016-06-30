package com.landscape.schoolexandroid.enums;

/**
 * Created by 1 on 2016/6/30.
 */
public enum TaskStatus {
    INIT(-1),
    RUN(0),
    COMPLETE(1),
    READED(2);

    private int status = -1;

    TaskStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static TaskStatus getStatus(int status) {
        switch (status) {
            case 0:
                return RUN;
            case 1:
                return COMPLETE;
            case 2:
                return READED;
            default:
                return INIT;
        }
    }
}
