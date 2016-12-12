package com.landscape.schoolexandroid.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 1 on 2016/7/6.
 */
public class TaskDb {

    public static void update(BriteDatabase db, int taskId, String duration) {
        Observable.just(1).map(integer -> {
            boolean hasTask = false;
            Cursor cursor = db.query("SELECT * FROM " + LabelTable.TASK_TABLE + " WHERE " + LabelTable.taskId + "=" + taskId);
            while (cursor.moveToNext()) {
                hasTask = true;
                break;
            }
            if (null != cursor) {
                cursor.close();
            }
            TaskInfo taskInfo = new TaskInfo();
            if (!TextUtils.isEmpty(duration)) {
                taskInfo.isExist = hasTask;
                taskInfo.taskId = taskId;
                taskInfo.duration = duration;
                return taskInfo;
            }
            return null;
        }).flatMap(taskBean -> Observable.create((Observable.OnSubscribe<Long>) subscriber -> {
            if (taskBean != null) {
                ContentValues values = new ContentValues();
                values.put(LabelTable.taskId, taskBean.taskId);
                values.put(LabelTable.duration, taskBean.duration);
                if (taskBean.isExist) {
                    subscriber.onNext((long) db.update(LabelTable.TASK_TABLE, values, LabelTable.taskId + " = ? ", new String[]{taskId + ""}));
                } else {
                    subscriber.onNext(db.insert(LabelTable.TASK_TABLE, values));
                }
            }
        })).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(query -> {
        });
    }

    public static void query(BriteDatabase db, int taskId, QueryCallbk call) {
        Observable<SqlBrite.Query> tasks = db.createQuery(
                LabelTable.TASK_TABLE,
                "SELECT * FROM " + LabelTable.TASK_TABLE + " WHERE " + LabelTable.taskId + "=" + taskId);
        tasks.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
            boolean hasTask = false;
            Cursor cursor = query.run();
            while (cursor.moveToNext()) {
                hasTask = true;
                break;
            }
            if (hasTask && call != null) {
                call.duration(cursor.getString(cursor.getColumnIndex(LabelTable.duration)));
            }
            if (null != cursor) {
                cursor.close();
            }
        });
    }

    static QueryCallbk callBk = null;

    public static void setQueryCallbk(QueryCallbk call) {
        callBk = call;
    }

    public interface QueryCallbk {
        void duration(String duration);
    }

    static class TaskInfo {
        public boolean isExist = false;
        public int taskId;
        public String duration = "";
    }
}
