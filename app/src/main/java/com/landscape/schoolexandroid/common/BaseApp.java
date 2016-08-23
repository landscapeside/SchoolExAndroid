package com.landscape.schoolexandroid.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.landscape.schoolexandroid.dagger.AppComponent;
import com.landscape.schoolexandroid.dagger.AppModule;
import com.landscape.schoolexandroid.dagger.DaggerAppComponent;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.Logger;
import com.utils.behavior.AppFileUtils;
import com.utils.system.CrashHandler;
import com.utils.system.ScreenParam;

/**
 * Created by Administrator on 2016/4/25.
 */
public class BaseApp extends Application {
    private AppComponent mAppComponent;

    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLog();
        AppFileUtils.init(AppConfig.ROOT_FOLDER);
        ScreenParam.init(this);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

