package com.landscape.schoolexandroid.dagger;

import android.content.Context;

import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.db.DbHelper;
import com.squareup.otto.Bus;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/1/30.
 */
@Module
public class AppModule {
    private BaseApp app;
    private Bus bus;

    public AppModule(BaseApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public BaseApp getApp() {
        return app;
    }

    @Provides
    @Singleton
    public Context getApplication() {
        return app.getApplicationContext();
    }

    @Singleton
    @Provides
    public Bus getBus() {
        return bus==null?bus= new Bus():bus;
    }

    @Provides
    @Singleton
    public BriteDatabase getBriteDatabase(DbHelper openHelper) {
        openHelper.getWritableDatabase();
        SqlBrite sqlBrite = SqlBrite.create();
        return sqlBrite.wrapDatabaseHelper(openHelper,Schedulers.io());
    }

    @Provides
    @Singleton
    public DbHelper getHdSQLHelper(Context context) {
        return new DbHelper(context, 1);
    }
}
