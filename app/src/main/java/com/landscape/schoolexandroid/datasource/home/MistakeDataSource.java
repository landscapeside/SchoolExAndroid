package com.landscape.schoolexandroid.datasource.home;

import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.api.HomeWorkApi;
import com.landscape.schoolexandroid.api.MistakeApi;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.datasource.BaseDataSource;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.mistake.MistakeListInfo;
import com.landscape.schoolexandroid.mode.mistake.MistakeQuestionBean;
import com.landscape.schoolexandroid.mode.mistake.MistakeQuestionListInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskListInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * Created by 1 on 2016/7/12.
 */
@Singleton
public class MistakeDataSource implements BaseDataSource {

    @Inject
    UserAccountDataSource userAccountDataSource;

    @Inject
    public MistakeDataSource() {}

    public Call<MistakeListInfo> request(Integer SubjectTypeID,Integer ExaminationPapersTypeID,Integer status,BaseCallBack<MistakeListInfo> callBack) {
        if (SubjectTypeID != null) {
            SubjectTypeID = SubjectTypeID == -1?null:SubjectTypeID;
        }
        if (ExaminationPapersTypeID != null) {
            ExaminationPapersTypeID = ExaminationPapersTypeID==-1?null:ExaminationPapersTypeID;
        }
        if (status != null) {
            status = status==10?null:status;
        }

        Call<MistakeListInfo> call = null;
        call = RetrofitService.createApi(MistakeApi.class)
                .getErrQuestionList(userAccountDataSource.getUserAccount().getData().getStudentId(),SubjectTypeID,ExaminationPapersTypeID,status);
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Call<MistakeQuestionBean> getQuestion(int taskId, BaseCallBack<MistakeQuestionBean> callBack) {
        Call<MistakeQuestionBean> call = null;
        call = RetrofitService.createApi(MistakeApi.class)
                .getErrorQuestions(taskId);
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }
}
