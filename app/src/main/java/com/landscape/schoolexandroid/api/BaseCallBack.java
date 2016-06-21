package com.landscape.schoolexandroid.api;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by landscape on 2016/6/21.
 */
public abstract class BaseCallBack<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        RetrofitService.cancel(call);
        if (response.body() != null) {
            response(response.body());
        } else {
            String errMsg = "当请求异常时解析信息返回IOException";
            try {
                errMsg = response.errorBody().string();
            } catch (IOException e) {
            }
            RetrofitService.netErr(errMsg);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        RetrofitService.cancel(call);
        RetrofitService.netErr(call,t);
    }

    public abstract void response(T response);
}
