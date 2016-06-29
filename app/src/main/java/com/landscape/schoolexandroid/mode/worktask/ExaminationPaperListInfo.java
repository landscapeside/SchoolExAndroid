package com.landscape.schoolexandroid.mode.worktask;

import com.landscape.schoolexandroid.mode.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2016/6/28.
 */
public class ExaminationPaperListInfo extends BaseBean {
    private List<ExaminationPaperInfo> data = new ArrayList<>();

    public List<ExaminationPaperInfo> getData() {
        return data;
    }

    public void setData(List<ExaminationPaperInfo> data) {
        this.data = data;
    }
}
