package com.landscape.schoolexandroid.datasource.worktask;

import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.api.HomeWorkApi;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.datasource.BaseDataSource;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.datasource.home.WorkTaskDataSource;
import com.landscape.schoolexandroid.mode.worktask.ExaminationPaperListInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskListInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * Created by 1 on 2016/6/27.
 */
@Singleton
public class TaskOptionDataSource implements BaseDataSource {

    @Inject
    UserAccountDataSource userAccountDataSource;

    @Inject
    public TaskOptionDataSource() {}

    public Call<ExaminationPaperListInfo> getPaper(ExaminationTaskInfo taskInfo, BaseCallBack<ExaminationPaperListInfo> callBack) {
        Call<ExaminationPaperListInfo> call = null;
        call = RetrofitService.createApi(HomeWorkApi.class)
                .getExaminationPaper(taskInfo.getExaminationPapersId(),taskInfo.getStudentQuestionsTasksID());
        RetrofitService.addCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Call<String> startWork(ExaminationTaskInfo taskInfo, BaseCallBack<String> callBack) {
        Call<String> call = null;
        call = RetrofitService.createApi(HomeWorkApi.class)
                .startWork(taskInfo.getStudentQuestionsTasksID());
        RetrofitService.addCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Call<String> endWork(ExaminationTaskInfo taskInfo, BaseCallBack<String> callBack) {
        Call<String> call = null;
        call = RetrofitService.createApi(HomeWorkApi.class)
                .endWork(taskInfo.getStudentQuestionsTasksID());
        RetrofitService.addCall(call);
        call.enqueue(callBack);
        return call;
    }

}
