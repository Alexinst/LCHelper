package com.t4f.lc_helper.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;

    public static final String DB_NAME = "Commands";
    public static final String HISTORY_TABLE = "History";

    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DATE = "date";

    public Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, 1, DB_VERSION);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            String CREATE_HISTORY_TABLE = "CREATE TABLE " + HISTORY_TABLE + "("
                    + FIELD_TITLE + " TEXT, "
                    + FIELD_DATE + " TEXT)";
            //  + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            db.execSQL(CREATE_HISTORY_TABLE);

        }

        if (oldVersion < 2) {
            //
        }
    }

    public void insertRecord(SQLiteDatabase db, String name) {


        // 查询此命令是否已在历史记录中
        String SEARCH_CMD = "SELECT * FROM " + HISTORY_TABLE +" WHERE TITLE = ?";
        Cursor cursor = db.rawQuery(SEARCH_CMD, new String[] {name});

        Date now = new Date();  // 当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues record = new ContentValues();

        // 当 cursor 不为空，更新该 record；反之，插入新 record
        if (cursor.moveToFirst()) {
            String message = "Update Record";
            Log.e("SearchResultActivity", message);

            record.put("date", sdf.format(now));

            db.update(HISTORY_TABLE, record, "title = ?",new String[]{name});

        } else {
            String message = "Insert Record";
            Log.e("SearchResultActivity", message);

            record.put("title", name);
            record.put("date", sdf.format(now));
            Log.e("SearchResultActivity", record.toString());

            db.insert(HISTORY_TABLE, null, record);
        }

        cursor.close();
    }
}

