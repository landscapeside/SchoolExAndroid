package com.landscape.schoolexandroid.datasource.home;

import android.content.Context;

import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.api.HomeWorkApi;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.datasource.BaseDataSource;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskListInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * Created by 1 on 2016/6/27.
 */
@Singleton
public class WorkTaskDataSource implements BaseDataSource {

    @Inject
    UserAccountDataSource userAccountDataSource;

    @Inject
    public WorkTaskDataSource() {}

    public Call<ExaminationTaskListInfo> request(BaseCallBack<ExaminationTaskListInfo> callBack) {
        Call<ExaminationTaskListInfo> call = null;
        call = RetrofitService.createApi(HomeWorkApi.class)
                .getExaminationTasks(userAccountDataSource.getUserAccount().getData().getStudentId(),null,null,null);
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }
}
