package com.jy.cpdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 *   author RichardJay
 *    email jiangfengfn12@163.com
 *  created 2017/7/27 16:11
 *  version v1.0
 * modified 2017/7/27
 *     note 数据库操作类
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    // 数据库名
    private final static String DB_NAME = "person_lists.db";
    // 表名
    public final static String TABLE_NAME = "person";
    // 数据库版本
    private final static int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库表
        String sqlStr = "create table if not exists " + TABLE_NAME + "(_id integer primary key, " + "name TEXT, " +
                "description TEXT)";
        db.execSQL(sqlStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
