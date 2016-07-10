package com.landscape.netedge.account;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;
import com.landscape.netedge.BaseEdge;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.account.UserFile;

/**
 * Created by landscape on 2016/7/9.
 */
public interface IUser extends BaseEdge {
    @NetBegin
    void uploadFile();
    @NetEnd
    void uploadSuc(UserFile result);
    @NetBegin
    void updateUser(String Photo);
    @NetEnd
    void updateSuc(BaseBean result);
}
