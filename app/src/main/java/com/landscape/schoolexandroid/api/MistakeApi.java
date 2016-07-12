package com.landscape.schoolexandroid.api;

import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.mistake.MistakeListInfo;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 1 on 2016/6/21.
 */
public interface MistakeApi {

    @POST("api/Students/ErrorQuestionsList")
    Call<MistakeListInfo> getErrQuestionList(
            @Query("userid") int userid,
            @Query("SubjectTypeID") Integer SubjectTypeID,
            @Query("ExaminationPapersTypeID") Integer ExaminationPapersTypeID,
            @Query("status") Integer status);
}
