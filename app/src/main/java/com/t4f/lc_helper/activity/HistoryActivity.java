package com.t4f.lc_helper.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.t4f.lc_helper.R;
import com.t4f.lc_helper.sql.CommandDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView listHistory;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 启用向上导航
        setHomeUpDisplay();

        // 读取历史记录
        listHistory = (ListView) findViewById(R.id.history);
        SQLiteOpenHelper cmdDBHelper = new CommandDatabaseHelper(this);
        try {
            db = cmdDBHelper.getReadableDatabase();
            cursor = db.query("History",
                              new String[]{"_id", "NAME"},
                              null, null, null,
                              null, null, null);

            SimpleCursorAdapter cursorAdapter =
                    new SimpleCursorAdapter(this,
                                            android.R.layout.simple_list_item_1,
                                            cursor,
                                            new String[]{"NAME"},
                                            new int[]{android.R.id.text1},
                                            0);
            listHistory.setAdapter(cursorAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "HistoryActivity: 找不到历史记录",
                                         Toast.LENGTH_LONG);
            toast.show();
        }

        addItemLinks();

//        String FILENAME = "history.txt";
//        String history = "";
//        try {
////            FileInputStream fis = openFileInput(FILENAME);
//            InputStream is = getResources().getAssets().open(FILENAME);
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
//            Toast toast = Toast.makeText(this, "IOException", Toast.LENGTH_LONG);
//            toast.show();
//        }
//
//        char tag = ',';
//        records = split(history, tag);
//
    }

    private void setHomeUpDisplay() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void addItemLinks() {
        // 添加历史记录的链接
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view,
                                            int position,
                                            long id) {
                        Intent intent = new Intent(HistoryActivity.this,
                                SearchResultActivity.class);
                        TextView textView = (TextView) view;
                        String name = textView.getText().toString();
                        intent.putExtra("cmd", name);
                        startActivity(intent);
                    }
                };
        listHistory.setOnItemClickListener(itemClickListener);
    }

    private List<String> split(String history, char tag) {
        List<String> records = new ArrayList<>();
        int start = 0, len = history.length();
        for (int i = 0; i < len; i++) {
            if (history.charAt(i) == tag) {
                records.add(history.substring(start, i));
                start = i + 1;
            }
        }

        if (start < len)
            records.add(history.substring(start, len));

        return records;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Cursor newCursor = db.query("History",
                                    new String[]{"NAME"},
                           null, null, null,
                             null, null, null);
        CursorAdapter adapter = (CursorAdapter) listHistory.getAdapter();
        adapter.changeCursor(newCursor);
        cursor = newCursor;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
