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

//    private List<String> readData() {
//        // 读取历史记录
//        String FILENAME = "history.txt";
//        String history = "";
//        try {
////            FileInputStream fis = openFileInput(FILENAME);
//            InputStream is = context.getResources().getAssets().open(FILENAME);
//            InputStreamReader isReader = new InputStreamReader(is);
//            BufferedReader bfReader = new BufferedReader(isReader);
//
//            StringBuilder sb = new StringBuilder();
//            String line = "";
//            while ((line = bfReader.readLine()) != null)
//                sb.append(line);
//
//            history = sb.toString();
//            is.close();
//        }
//        catch (IOException e) {
//            Toast toast = Toast.makeText(context, "找不到历史记录", Toast.LENGTH_LONG);
//            toast.show();
//        }
//
//        char tag = ',';
//        List<String> records = split(history, tag);
//
//
//        return records;
//    }
//
//    private List<String> split(String history, char tag) {
//        List<String> records = new ArrayList<>();
//        int start = 0, len = history.length();
//        for (int i = 0; i < len; i++) {
//            if (history.charAt(i) == tag) {
//                records.add(history.substring(start, i));
//                start = i + 1;
//            }
//        }
//
//        if (start < len)
//            records.add(history.substring(start, len));
//
//        return records;
//    }
}
