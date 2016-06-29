package com.landscape.schoolexandroid.common;

import android.app.Application;

import com.landscape.schoolexandroid.dagger.AppComponent;
import com.landscape.schoolexandroid.dagger.AppModule;
import com.landscape.schoolexandroid.dagger.DaggerAppComponent;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.Logger;
import com.utils.behavior.AppFileUtils;
import com.utils.system.ScreenParam;

/**
 * Created by Administrator on 2016/4/25.
 */
public class BaseApp extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
        AppFileUtils.init(AppConfig.ROOT_FOLDER);
        ScreenParam.init(this);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void initLog() {
        Logger.init(getPackageName())
                .methodCount(3)
                .methodOffset(2)
                .logTool(new AndroidLogTool());
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}

