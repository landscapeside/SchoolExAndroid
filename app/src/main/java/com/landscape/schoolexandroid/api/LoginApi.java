package com.landscape.schoolexandroid.api;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 1 on 2016/6/21.
 */
public interface LoginApi {
    @GET("api/Account/Login")
    Observable<String> accountLogin(@Query("loginName") String loginName,@Query("pwd") String pwd);

}
