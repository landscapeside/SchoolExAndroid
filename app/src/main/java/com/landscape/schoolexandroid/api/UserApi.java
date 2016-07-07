package com.landscape.schoolexandroid.api;

import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.account.UserFile;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by 1 on 2016/6/21.
 */
public interface UserApi {
    @Multipart
    @POST("http://service.student.cqebd.cn/HomeWork/UpdataFile")
    Call<UserFile> uploadFile(@Part("files\"; filename=\"image.jpg\"") RequestBody files);

    @POST("api/Account/EditPwd")
    Call<BaseBean> modifyPwd(@Query("UserId") int UserId, @Query("Pwd") String Pwd, @Query("NewPwd") String NewPwd);
}
