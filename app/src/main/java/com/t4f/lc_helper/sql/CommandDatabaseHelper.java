package com.t4f.lc_helper.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CommandDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Commands";
    private static final int DB_VERSION = 2;
    public Context context;

    public CommandDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
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
            db.execSQL("CREATE TABLE History (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT);");

        }
        if (oldVersion < 2) {
            //
        }
    }

    private void insertRecord(SQLiteDatabase db, String name) {
        ContentValues cmdValues = new ContentValues();
        cmdValues.put("NAME", name);

        db.insert("History", null, cmdValues);
    }
}

