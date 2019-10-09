package com.t4f.lc_helper.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.Toast;

import com.t4f.lc_helper.Adapter.MyHistoryAdapter;
import com.t4f.lc_helper.R;
import com.t4f.lc_helper.sql.DBHelper;


public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SQLiteDatabase db;
    private Cursor cursor;
    private MyHistoryAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 启用向上导航
        setHomeUpDisplay();

        // 读取历史记录
        SQLiteOpenHelper dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.history_recycler);

        // 生成 cursor，为 recyclerView 构建 adapter
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_NAME,
                              new String[]{"title", "date"},
                      null,
                    null,
                       null,
                        null,
                               DBHelper.FIELD_DATE + " DESC",
                          null); //  + " DESC"

            cursorAdapter = new MyHistoryAdapter(this, cursor);
            recyclerView.setAdapter(cursorAdapter);

        } catch (SQLiteException e) {
            String message = "HistoryActivity: 找不到历史记录";
            Toast toast = Toast.makeText(this, message,
                                         Toast.LENGTH_LONG);
            toast.show();
        }

        // 设置 recyclerView 为 LinearLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 设置 recyclerView 的点击响应动作
        setOnItemClickListener();
    }

    private void setHomeUpDisplay() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setOnItemClickListener() {
        // 添加历史记录的链接
        if (cursorAdapter != null) {
            cursorAdapter.setListener(new MyHistoryAdapter.Listener() {
                @Override
                public void onClick(String title) {
                    Intent intent = new Intent(HistoryActivity.this,
                                               SearchResultActivity.class);
                    intent.putExtra("cmd", title);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
//        Cursor newCursor = db.query("History",
//                                    new String[]{"NAME"},
//                                    null, null, null,
//                                    null, null, null);
////        CursorAdapter adapter = (CursorAdapter) listHistory.getAdapter();
//        CursorRecyclerViewAdapter adapter = (CursorRecyclerViewAdapter) recyclerView.getAdapter();
//        adapter.changeCursor(newCursor);
//        cursor = newCursor;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (db != null) db.close();
    }
}
