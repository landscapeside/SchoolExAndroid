package com.landscape.netedge.worktask;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;
import com.landscape.netedge.BaseEdge;
import com.landscape.schoolexandroid.mode.BaseBean;

/**
 * Created by 1 on 2016/7/1.
 */
public interface IAnswer extends BaseEdge {
    @NetBegin
    void finish();
    @NetEnd
    void finishResult(BaseBean result);
}
