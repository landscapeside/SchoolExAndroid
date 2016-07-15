package com.utils.datahelper;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * RxUtils
 *
 * Usage:
 *
 * RxCounter.counter(60, 0).doOnNext(this::updateVoiceCode)
 *    .doOnCompleted(this::completeVoiceCode).subscribe();
 *
 *
 * Created by sharyuke on 15-8-30.
 */
public class RxCounter {

    /**
     * 计数组件
     *
     * @param from 开始计数处
     * @param to   结束计数处
     * @return observable
     */
    public static Observable<Long> counter(long from, long to) {
        return counter(from, to, 1, TimeUnit.SECONDS);
    }

    public static Observable<Long> counter(long from, long to, int delay, TimeUnit time) {
        return from == to ? Observable.empty() : Observable.<Long>create(subscriber -> {
            tick = true;
            int step = from > to ? -1 : 1;
            long cursor = from;
            while (cursor != to && tick) {
                subscriber.onNext(cursor);
                cursor += step;
                try {
                    time.sleep(delay);
//                    SystemClock.sleep(1000);//this method has no exception
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            subscriber.onNext(cursor);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void cancelTick(){
        tick = false;
    }

    private static boolean tick = true;

    public static Observable<Integer> tick(int tickTime,TimeUnit unit){
        return Observable.<Integer>create(subscriber -> {
            while (tick){
                subscriber.onNext(0);
                try {
                    unit.sleep(tickTime);
//                    SystemClock.sleep(1000);//this method has no exception
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
            subscriber.onNext(0);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
