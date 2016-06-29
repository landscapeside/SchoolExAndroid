package com.landscape.schoolexandroid.mode.worktask;

import com.landscape.schoolexandroid.mode.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2016/6/27.
 */
public class ExaminationTaskListInfo extends BaseBean {
    private List<ExaminationTaskInfo> data = new ArrayList<>();

    public List<ExaminationTaskInfo> getData() {
        return data;
    }

    public void setData(List<ExaminationTaskInfo> data) {
        this.data = data;
    }
}
