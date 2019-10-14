package com.t4f.lc_helper.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    public static final String DB_NAME = "Commands";
    private static final int DB_VERSION = 1;

    public static final String TABLE_COMMANDS = "Commands";
    public static final String TABLE_HISTORY = "Histories";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_DATE = "date";

    public Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COMMANDS_TABLE = String.format("CREATE TABLE %s ("
                        + "%s INTEGER PRIMARY KEY AUTOINCREMENT "
                        + "%s TEXT "
                        + "%s TEXT)",
                TABLE_COMMANDS, KEY_ID, KEY_TITLE, KEY_CONTENT
        );
        db.execSQL(CREATE_COMMANDS_TABLE);

        String CREATE_HISTORY_TABLE = String.format("CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT "
                + "%s TEXT, "
                + "%s TEXT)",
                TABLE_HISTORY, KEY_ID, KEY_TITLE, KEY_DATE
        );
        db.execSQL(CREATE_HISTORY_TABLE);

        populateTableCommands();
    }

    private void populateTableCommands() {
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }

        return instance;
    }

    public long addOrUpdateHistory(String name, String now) {
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        db.beginTransaction();
        try {
            ContentValues record = new ContentValues();
            record.put(KEY_DATE, now);

            int rows = db.update(TABLE_HISTORY, record, KEY_TITLE + " = ?",
                    new String[]{name});

            if (rows == 1) {
                String SEARCH_ID = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_ID, TABLE_HISTORY, KEY_TITLE);
                Cursor cursor = db.rawQuery(SEARCH_ID, new String[]{name});
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID));

                cursor.close();
            }
            else {
                // update() 失败，意味着该指令未被查询过
                record.put(KEY_TITLE, name);
                id = db.insertOrThrow(TABLE_HISTORY, null, record);
            }

            db.setTransactionSuccessful();
            record.clear();

        } catch (Exception e) {
            return -1;

        } finally {
            db.endTransaction();
        }

        db.close();
        return id;
    }
}

