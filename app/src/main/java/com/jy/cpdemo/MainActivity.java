package com.jy.cpdemo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvShow;
    private ContentResolver contentResolver;
    private Uri uri;
    private List<Person> persons;
    // 观察者
    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Toast.makeText(MainActivity.this, "---观察者收到了通知---", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvShow = (TextView) findViewById(R.id.tv_show);

        contentResolver = getContentResolver();
        String uriStr = "content://" + MyContentProvider.AUTHORITY + "/" + DbOpenHelper.TABLE_NAME + "/";
        uri = Uri.parse(uriStr);
        // 注册观察者
        contentResolver.registerContentObserver(uri, true, contentObserver);
    }

    /**
     * 插入
     * @param view
     */
    public void insert(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", 3);
        contentValues.put("name", "jy");
        contentValues.put("description", "good");

        contentResolver.insert(uri, contentValues);
    }

    /**
     * 查询
     * @param view
     */
    public void query(View view) {
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        persons = new ArrayList<>();
        while (cursor.moveToNext()) {
            Person person = new Person();
            person.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            person.setName(cursor.getString(cursor.getColumnIndex("name")));
            person.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            persons.add(person);
        }
        cursor.close();
        tvShow.setText(persons.toString());
    }

    public void delete(View view) {

    }

    public void update(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销
        contentResolver.unregisterContentObserver(contentObserver);
    }
}
