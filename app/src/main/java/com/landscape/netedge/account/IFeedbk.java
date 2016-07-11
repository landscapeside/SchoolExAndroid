package com.landscape.netedge.account;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;
import com.landscape.netedge.BaseEdge;
import com.landscape.schoolexandroid.mode.BaseBean;

/**
 * Created by 1 on 2016/7/11.
 */
public interface IFeedbk extends BaseEdge {
    @NetBegin
    void submit(String content,String type);
    @NetEnd
    void submitSuc(BaseBean result);
}
