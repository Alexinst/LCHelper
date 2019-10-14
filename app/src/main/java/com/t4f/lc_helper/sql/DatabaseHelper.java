package com.t4f.lc_helper.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    private String COMMAND_PATH = "commands";

    public static final String DB_NAME = "Commands.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_COMMAND = "Command";
    public static final String TABLE_HISTORY = "History";

    public static final String KEY_COMMAND_ID = "id";
    public static final String KEY_COMMAND_TITLE = "title";
    public static final String KEY_COMMAND_CONTENT = "content";

    public static final String KEY_HISTORY_ID = "id";
    public static final String KEY_HISTORY_TITLE = "title";
    public static final String KEY_HISTORY_DATE = "date";

    public Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COMMANDS_TABLE = String.format("CREATE TABLE %s ("
                        + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "%s TEXT, "
                        + "%s TEXT)",
                TABLE_COMMAND, KEY_COMMAND_ID, KEY_COMMAND_TITLE, KEY_COMMAND_CONTENT
        );
        db.execSQL(CREATE_COMMANDS_TABLE);

        String CREATE_HISTORY_TABLE = String.format("CREATE TABLE %s ("
                        + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "%s TEXT, "
                        + "%s TEXT)",
                TABLE_HISTORY, KEY_HISTORY_ID, KEY_HISTORY_TITLE, KEY_HISTORY_DATE
        );
        db.execSQL(CREATE_HISTORY_TABLE);

        populateTableCommand(db);
    }

    private void populateTableCommand(SQLiteDatabase db) {
        String[] filenames = null;

        AssetManager manager = context.getAssets();
        try {
            filenames = manager.list(COMMAND_PATH);

        } catch (IOException e) {
            // log
            String message = "errors: read filenames";
            Log.e("DatabaseHelper", message);

        } finally {
            if (filenames != null) {
                for (String filename : filenames) {
                    String cmdName = getName(filename);
                    String content = getContent(String.format("%s/%s",
                            COMMAND_PATH, filename));

                    long id;
                    do {
                        id = addOrUpdateCommand(db, cmdName, content);
                    } while (id == -1);
                }
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }

        return instance;
    }

    private String getName(String filename) {
        StringBuilder sb = new StringBuilder();

        for (char c : filename.toCharArray()) {
            if (c == '.') break;

            sb.append(c);
        }

        return sb.toString();
    }

    private String getContent(String filename) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(filename)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            String message = "errors: get content";
            Log.e("DatabaseHelper", message);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // log
                }
            }
        }

        return sb.toString();
    }

    public long addOrUpdateCommand(SQLiteDatabase db, String title, String content) {
        long id = -1;

        db.beginTransaction();
        try {
            ContentValues record = new ContentValues();
            record.put(KEY_HISTORY_TITLE, title);
            record.put(KEY_COMMAND_CONTENT, content);

            id = db.insertOrThrow(TABLE_COMMAND, null, record);

            record.clear();
            db.setTransactionSuccessful();

        } catch (SQLException e) {
            String message = "errors: add or update command";
            Log.e("DatabaseHelper", message);
            return id;

        } finally {
            db.endTransaction();
        }

        return id;
    }

    public long addOrUpdateHistory(String title, String now) {
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        db.beginTransaction();
        try {
            ContentValues record = new ContentValues();
            record.put(KEY_HISTORY_DATE, now);

            int rows = db.update(TABLE_HISTORY, record, KEY_HISTORY_TITLE + " = ?",
                    new String[]{title});

            if (rows == 1) {
                String SEARCH_ID = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_HISTORY_ID, TABLE_HISTORY, KEY_HISTORY_TITLE);
                Cursor cursor = db.rawQuery(SEARCH_ID, new String[]{title});
                id = cursor.getInt(cursor.getColumnIndex(KEY_HISTORY_ID));

                cursor.close();
            } else {
                // update() 失败，意味着该指令未被查询过
                record.put(KEY_HISTORY_TITLE, title);
                id = db.insertOrThrow(TABLE_HISTORY, null, record);
            }

            db.setTransactionSuccessful();
            record.clear();

        } catch (Exception e) {
            return id;

        } finally {
            db.endTransaction();
        }

        db.close();
        return id;
    }
}

