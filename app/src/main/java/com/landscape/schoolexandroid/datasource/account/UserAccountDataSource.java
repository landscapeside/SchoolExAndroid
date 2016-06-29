package com.landscape.schoolexandroid.datasource.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.BaseDataSource;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.utils.datahelper.JSONS;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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

}
