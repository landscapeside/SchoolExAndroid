package com.landscape.schoolexandroid.api;

import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.worktask.ExaminationPaperListInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskListInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 1 on 2016/6/21.
 */
public interface HomeWorkApi {

    @POST("api/Students/GetExaminationTasks")
    Call<ExaminationTaskListInfo> getExaminationTasks(
            @Query("userid") int userid,
            @Query("SubjectTypeID") String SubjectTypeID,
            @Query("ExaminationPapersTypeID") String ExaminationPapersTypeID,
            @Query("status") String status);

    @GET("api/Students/GetExaminationPapersByID")
    Call<ExaminationPaperListInfo> getExaminationPaper(@Query("id") int id, @Query("tasksid") int tasksid);

    @GET("api/Students/StartWork")
    Call<BaseBean> startWork(@Query("StudentQuestionsTasksID") int StudentQuestionsTasksID);

    @GET("api/Students/EndWork")
    Call<BaseBean> endWork(@Query("StudentQuestionsTasksID") int StudentQuestionsTasksID);

    @POST("api/Students/SubmitAnswer")
    Call<BaseBean> submitAnswer(
            @Query("Stu_Id") int Stu_Id,
            @Query("QuestionId") int QuestionId,
            @Query("ExaminationPapersId") int ExaminationPapersId,
            @Query("StudentQuestionsTasksId") int StudentQuestionsTasksId,
            @Query("Answer") String Answer,
            @Query("QuestionAnswerTypeId") int QuestionAnswerTypeId);

}
