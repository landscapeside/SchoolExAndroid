package com.landscape.netedge.account;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;

/**
 * Created by 1 on 2016/6/21.
 */
public interface ILogin {
    @NetBegin
    void login();
    @NetEnd
    void loginResult(String result);
}
