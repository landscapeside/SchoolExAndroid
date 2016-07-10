package com.landscape.schoolexandroid.datasource.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.api.HomeWorkApi;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.api.UserApi;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.BaseDataSource;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.mode.account.UserFile;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.utils.datahelper.JSONS;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by 1 on 2016/6/21.
 */
@Singleton
public class UserAccountDataSource implements BaseDataSource {

    SharedPreferences preferences;

    @Inject
    public UserAccountDataSource(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public UserAccount getUserAccount() {
        String jsonRecord = preferences.getString(Constant.SHARE_PRE_USER_ACCOUNT, "");
        if (TextUtils.isEmpty(jsonRecord)) {
            return null;
        }
        return JSONS.parseObject(jsonRecord, UserAccount.class);
    }

    public void saveUserAccount(UserAccount userAccount) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constant.SHARE_PRE_USER_ACCOUNT, JSONS.parseJson(userAccount));
        editor.commit();
    }

    public void clearData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constant.SHARE_PRE_USER_ACCOUNT, "");
        editor.commit();
    }

    public int getFlowerLvSmall(int flowers) {
        return flowers%10;
    }

    public int getFlowerLvNormal(int flowers) {
        return (flowers%100)/10;
    }

    public int getFlowerLvBig(int flowers) {
        return flowers/100;
    }

    public Call<UserFile> uploadFile(File file, BaseCallBack<UserFile> callBack) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        Call<UserFile> call = null;
        call = RetrofitService.createApi(UserApi.class)
                .uploadFile(requestBody);
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Call<BaseBean> modifyPwd(String pwd,String newPwd,BaseCallBack<BaseBean> callBack) {
        UserAccount userAccount = getUserAccount();
        Call<BaseBean> call = null;
        call = RetrofitService.createApi(UserApi.class)
                .modifyPwd(userAccount.getData().getStudentId(),pwd,newPwd);
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }

    public Call<BaseBean> updateStudent(String Photo,BaseCallBack<BaseBean> callBack) {
        UserAccount userAccount = getUserAccount();
        Call<BaseBean> call = null;
        call = RetrofitService.createApi(UserApi.class)
                .updateStudent(userAccount.getData().getStudentId(),Photo);
        RetrofitService.addCall(call);
        callBack.setCall(call);
        call.enqueue(callBack);
        return call;
    }

}
