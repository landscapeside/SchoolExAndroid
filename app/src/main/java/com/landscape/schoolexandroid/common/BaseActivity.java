package com.landscape.schoolexandroid.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.edge.proxy.NetProxy;
import com.edge.reader.NetBuilder;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.utils.behavior.ProgressUtils;

/**
 * Created by 1 on 2016/5/27.
 */
public class BaseActivity extends AppCompatActivity {

    public NetProxy mProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProxy = new NetProxy(new NetBuilder().register(new NetBuilder.NetEdge.INetListener() {
            @Override
            public void netBegin() {
                ProgressUtils.showProgressDialog(BaseActivity.this, "请稍后", () -> RetrofitService.cancel());
            }

            @Override
            public void netEnd() {
                ProgressUtils.dismissProgressDialog();
            }
        }).build());
    }
}
