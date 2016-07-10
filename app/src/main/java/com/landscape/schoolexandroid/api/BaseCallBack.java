package com.landscape.schoolexandroid.api;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.utils.behavior.ToastUtil;

import java.io.IOException;
import java.util.Set;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by landscape on 2016/6/21.
 */
public abstract class BaseCallBack<T> extends CallContext implements Callback<T> {

    public BaseCallBack(Context context) {
        super(context);
    }

    public BaseCallBack(Context context, Call call) {
        super(context, call);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() != null) {
            response(response.body());
        } else {
            String errMsg = "当请求异常时解析信息返回IOException";
            try {
                errMsg = response.errorBody().string();
            } catch (IOException e) {
            }
            if (TextUtils.isEmpty(errMsg)) {
                Headers headers = response.headers();
                Set<String> names = headers.names();
                if (names.contains("X-Ca-Error-Message")) {
                    ToastUtil.show(mContext, headers.get("X-Ca-Error-Message"));
                } else {
                    ToastUtil.show(mContext,"网络请求失败，错误码："+response.code());
                }
            } else {
                RetrofitService.netErr(mContext,errMsg);
            }
            err();
        }
        destroy();
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Logger.e("BaseCallBack======>onFailure");
        RetrofitService.netErr(mCall,t);
        if (RetrofitService.isLive(mCall)) {
            err();
        }
        destroy();
    }

    @Override
    public BaseCallBack setContext(Context context) {
        return (BaseCallBack) super.setContext(context);
    }

    public abstract void response(T response);
    public abstract void err();
}
