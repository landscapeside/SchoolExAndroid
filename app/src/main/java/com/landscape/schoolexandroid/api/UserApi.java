package com.landscape.schoolexandroid.api;

import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.account.UserFile;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by 1 on 2016/6/21.
 */
public interface UserApi {
    @Multipart
    @POST("http://service.student.cqebd.cn/HomeWork/UpdataFile")
    Call<UserFile> uploadFile(@Part("files\"; filename=\"image.jpg\"") RequestBody files);
}
