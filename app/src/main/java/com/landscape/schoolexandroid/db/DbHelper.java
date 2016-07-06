package com.landscape.schoolexandroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 在这里创建所需要的各种表，并进行表的版本更新。
 * 
 * @author 
 */
public class DbHelper extends SQLiteOpenHelper {
    // 数据库名
    public static final String DATABASE_NAME = "schoolex.db";

    public DbHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    private void createTable(SQLiteDatabase db) {
        // papertask
        StringBuilder customerBuilder = new StringBuilder();
        customerBuilder.append("create table if not exists "+LabelTable.TASK_TABLE); // 表名
        customerBuilder.append("("+LabelTable.taskId +" INTEGER PRIMARY KEY,");
        customerBuilder.append(LabelTable.duration+" varchar(20))");
        db.execSQL(customerBuilder.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	if(newVersion > 1){
    		db.execSQL("drop table if exists " + LabelTable.TASK_TABLE);
    	}
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}