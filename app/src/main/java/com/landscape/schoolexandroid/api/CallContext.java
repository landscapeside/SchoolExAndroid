package com.landscape.schoolexandroid.api;

import android.content.Context;

import retrofit2.Call;

/**
 * Created by landscape on 2016/7/9.
 */
public class CallContext {
    protected Context mContext=null;
    protected Call mCall;

    public CallContext(Context context) {
        mContext = context;
    }

    public CallContext(Context context,Call call) {
        mContext = context;
        setCall(call);
    }

    public CallContext setContext(Context context) {
        mContext = context;
        return this;
    }

    public void setCall(Call call) {
        mCall = call;
    }

    public void destroy() {
        mContext = null;
        RetrofitService.cancel(mCall);
        mCall = null;
    }
}
