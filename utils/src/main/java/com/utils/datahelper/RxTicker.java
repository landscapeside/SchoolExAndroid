package com.utils.datahelper;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 1 on 2016/3/7.
 */
public class RxTicker {

    private static final int DELAY = 5;

    public static  <T> Observable<T> tick(Observable<T> src,int delay){
        return src.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .repeat().sample(delay, TimeUnit.SECONDS, AndroidSchedulers.mainThread());
    }

    public static <T> Observable<T> tick(Observable<T> src){
        return tick(src, DELAY);
    }

    public static void cancelTick(Subscription subscription){
        subscription.unsubscribe();
    }

}
