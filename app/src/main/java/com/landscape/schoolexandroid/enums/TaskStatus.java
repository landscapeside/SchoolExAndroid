package com.landscape.schoolexandroid.enums;

/**
 * Created by 1 on 2016/6/30.
 */
public enum TaskStatus {
    INIT(-1,"共%s题"),
    RUN(0,"答题中"),
    COMPLETE(1,"已完成"),
    READED(2,"已批阅");

    private int status = -1;
    String name;

    TaskStatus(int status,String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
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
