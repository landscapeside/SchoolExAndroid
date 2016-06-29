package com.landscape.schoolexandroid.api;

import android.content.Context;
import android.text.TextUtils;

import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.orhanobut.logger.Logger;
import com.utils.behavior.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SkyEyesStion on 2016/2/26.
 */
public class RetrofitService {

    private static List<Call> calls = new ArrayList<>();

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new HeaderInterceptor())
            .addInterceptor
            (new HttpLoggingInterceptor(message -> {
                if (!TextUtils.isEmpty(message) && message.startsWith("{")) {
                    Logger.json(message);
                } else {
                    Logger.i(message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private RetrofitService() {
        //construct
    }

    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    public static void addCall(Call call) {
        calls.add(call);
    }

    public static void cancel() {
        for (Call call : calls) {
            call.cancel();
        }
        calls.clear();
    }

    public static void cancel(Call call) {
        call.cancel();
        if (calls.contains(call)) {
            calls.remove(call);
        }
    }

    public static void netErr(Throwable throwable){
        Logger.e(throwable.getMessage());
    }

    public static <T> void netErr(Call<T> call, Throwable throwable){
        Logger.e(throwable.getMessage());
    }

    public static <T> void netErr(Context context, String errMsg){
        if (!TextUtils.isEmpty(errMsg)) {
            ToastUtil.show(context,errMsg);
            Logger.e(errMsg);
        }
    }
}
