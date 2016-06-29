package com.landscape.schoolexandroid.datasource;

/**
 * Created by Administrator on 2016/5/16.
 */
public interface BaseDataSource {

    interface CallBack<T>{
        void callBack(T data);
        void err();
    }

}
