package com.jy.cpdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    private static final String TAG = "MyContentProvider";
    // 授权
    public static final String AUTHORITY = "com.jy.cpdemo";
    // 数据库表的标识
    public static final int PERSON_CODE = 1;
    // UriMatcher实例：在ContentProvider中注册URI
    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 初始化
        //关联不同的URI和code，便于后续getType返回不同的类型
        mMatcher.addURI(AUTHORITY, DbOpenHelper.TABLE_NAME, PERSON_CODE);
    }

    // 数据库操作实例
    private SQLiteDatabase mDatabase;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "---delete---");
        String tableName = getTableName(uri);
        if (mDatabase == null || tableName == null)
            return -1;
        // 删除行并返回影响的行数
        int deleteCount = mDatabase.delete(tableName, selection, selectionArgs);
        if (deleteCount == 0)
            return -1;
        // 通知观察者
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "---insert---");
        String tableName = getTableName(uri);
        if (mDatabase == null || tableName == null)
            Log.e(TAG, "insert error!");
        mDatabase.insert(tableName, null, values);
        // 通知观察者
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Log.d(TAG, "---onCreate---");
        initProvider();
        return false;
    }

    /**
     * 初始化数据库
     */
    private void initProvider() {
        mDatabase = new DbOpenHelper(getContext()).getWritableDatabase();
        // 开启新的子线程来初始化数据库
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 先清除数据
                mDatabase.execSQL("delete from " + DbOpenHelper.TABLE_NAME);
                // 再插入数据
                mDatabase.execSQL("insert into " + DbOpenHelper.TABLE_NAME + " values(1,'JY','handsome boy')");
            }
        }).start();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "---query---");
        String tableName = getTableName(uri);
        if (mDatabase == null || tableName == null)
            return null;
        return mDatabase.query(tableName, projection, selection, selectionArgs, null, sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "---update---");
        String tableName = getTableName(uri);
        if (mDatabase == null || tableName == null)
            return -1;
        // 更新行并返回影响的行数
        int updateCount = mDatabase.update(tableName, values, selection, selectionArgs);
        if (updateCount == 0)
            return -1;
        // 通知观察者
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    /**
     * 根据uri返回表名
     *
     * @param uri
     * @return 数据库表
     */
    private String getTableName(final Uri uri) {
        switch (mMatcher.match(uri)) {
            case PERSON_CODE:
                return DbOpenHelper.TABLE_NAME;
            default:
                return null;
        }
    }
}
