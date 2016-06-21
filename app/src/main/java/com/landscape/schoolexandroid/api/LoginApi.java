package com.landscape.schoolexandroid.api;

import com.landscape.schoolexandroid.mode.account.UserAccount;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 1 on 2016/6/21.
 */
public interface LoginApi {
    @GET("api/Account/Login")
    Call<UserAccount> accountLogin(@Query("loginName") String loginName, @Query("pwd") String pwd);

}
