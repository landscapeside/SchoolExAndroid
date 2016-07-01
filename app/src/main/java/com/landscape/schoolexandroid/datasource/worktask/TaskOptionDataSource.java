package com.landscape.schoolexandroid.datasource.worktask;

import android.content.Intent;
import android.os.Parcelable;

import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.api.HomeWorkApi;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.BaseDataSource;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.datasource.home.WorkTaskDataSource;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.worktask.ExaminationPaperListInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskListInfo;
import com.landscape.schoolexandroid.mode.worktask.QuestionGroupInfo;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;

import java.util.ArrayList;
import java.util.List;

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

    public Call<BaseBean> endWork(ExaminationTaskInfo taskInfo, BaseCallBack<BaseBean> callBack) {
        Call<BaseBean> call = null;
        call = RetrofitService.createApi(HomeWorkApi.class)
                .endWork(taskInfo.getStudentQuestionsTasksID());
        RetrofitService.addCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Intent putWorkData(Intent data, List<QuestionGroupInfo> questionGroupInfos,ExaminationTaskInfo taskInfo) {
        List<QuestionInfo> infos = new ArrayList<>();
        for (QuestionGroupInfo groupInfo : questionGroupInfos) {
            infos.addAll(groupInfo.getQuestion());
        }
        data.putExtra(Constant.TASK_INFO, taskInfo);
        data.putParcelableArrayListExtra(Constant.QUESTION_INFO, (ArrayList<? extends Parcelable>) infos);
        return data;
    }

}
