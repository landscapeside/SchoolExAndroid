package com.landscape.schoolexandroid.mode.mistake;

import com.landscape.schoolexandroid.mode.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2016/7/12.
 */
public class MistakeListInfo extends BaseBean {
    private List<MistakeInfo> data = new ArrayList<>();

    public List<MistakeInfo> getData() {
        return data;
    }

    public void setData(List<MistakeInfo> data) {
        this.data = data;
    }
}
