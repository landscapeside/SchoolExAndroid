package com.landscape.schoolexandroid.datasource.worktask;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.api.FileCallBack;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Call<BaseBean> startWork(ExaminationTaskInfo taskInfo, BaseCallBack<BaseBean> callBack) {
        Call<BaseBean> call = null;
        call = RetrofitService.createApi(HomeWorkApi.class)
                .startWork(taskInfo.getStudentQuestionsTasksID());
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Call<BaseBean> endWork(ExaminationTaskInfo taskInfo, BaseCallBack<BaseBean> callBack) {
        Call<BaseBean> call = null;
        call = RetrofitService.createApi(HomeWorkApi.class)
                .endWork(taskInfo.getStudentQuestionsTasksID());
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Intent putWorkData(Intent data,
                              List<QuestionGroupInfo> questionGroupInfos,
                              ExaminationTaskInfo taskInfo,
                              int subjectTypeId) {
        List<QuestionInfo> infos = new ArrayList<>();
        for (QuestionGroupInfo groupInfo : questionGroupInfos) {
            infos.addAll(groupInfo.getQuestion());
        }
        data.putExtra(Constant.TASK_INFO, taskInfo);
        data.putParcelableArrayListExtra(Constant.QUESTION_INFO, (ArrayList<? extends Parcelable>) infos);
        data.putExtra(Constant.SUBJECT_TYPE_ID, subjectTypeId);
        return data;
    }

    public Call<BaseBean> submitAnswer(ExaminationTaskInfo taskInfo,String answer,QuestionInfo info, BaseCallBack<BaseBean> callBack) {
        Call<BaseBean> call = null;
        call = RetrofitService.createApi(HomeWorkApi.class)
                .submitAnswer(
                        userAccountDataSource.getUserAccount().getData().getStudentId(),info.getId(),
                        taskInfo.getExaminationPapersId(),
                        taskInfo.getStudentQuestionsTasksID(),
                        answer,
                        info.getQuestionTypeId());
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Observable downloadFile(Context context, String url, File downLoadFile) {
        return Observable.create(subscriber -> {
            FileCallBack callBack = new FileCallBack(context,downLoadFile) {
                @Override
                public void response(Response response) {
                    subscriber.onNext(response);
                }

                @Override
                public void err() {
                    subscriber.onError(null);
                }
            };
            OkHttpClient okHttpClient = RetrofitService.getOkHttpClient();
            Request request = new Request.Builder().url(url).build();
            okhttp3.Call call = okHttpClient.newCall(request);
            RetrofitService.addCall(call);
            callBack.setCall(call);
            call.enqueue(callBack);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
