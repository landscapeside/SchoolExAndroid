package com.landscape.netedge.account;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;
import com.landscape.netedge.BaseEdge;
import com.landscape.schoolexandroid.mode.BaseBean;

/**
 * Created by 1 on 2016/7/7.
 */
public interface IPwd extends BaseEdge {
    @NetBegin
    void modify(String oldPwd,String newPwd);
    @NetEnd
    void modifySuc(BaseBean baseBean);
}
