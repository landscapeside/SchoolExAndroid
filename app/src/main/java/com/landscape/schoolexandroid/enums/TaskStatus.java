package com.landscape.schoolexandroid.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2016/6/30.
 */
public enum TaskStatus {
    INIT(-1,"共%s题","未开始"),
    RUN(0,"答题中","答题中"),
    COMPLETE(1,"已完成","已完成"),
    READED(2,"已批阅","已批阅"),
    NONE(10,"","默认");

    private int status = -1;
    String name;
    String filterName;

    TaskStatus(int status,String name,String filterName) {
        this.status = status;
        this.name = name;
        this.filterName = filterName;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getFilterName() {
        return filterName;
    }

    public static TaskStatus getStatus(int status) {
        switch (status) {
            case 0:
                return RUN;
            case 1:
                return COMPLETE;
            case 2:
                return READED;
            case -1:
                return INIT;
            default:
                return NONE;
        }
    }

    public static List<TaskStatus> getStatusFilters() {
        List<TaskStatus> statuses = new ArrayList<>();
        statuses.add(NONE);
        statuses.add(INIT);
        statuses.add(RUN);
        statuses.add(COMPLETE);
        statuses.add(READED);
        return statuses;
    }
}
