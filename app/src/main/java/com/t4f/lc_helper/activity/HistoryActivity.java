package com.t4f.lc_helper.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.t4f.lc_helper.adapter.MyRecyclerViewAdapter;
import com.t4f.lc_helper.R;
import com.t4f.lc_helper.sql.DatabaseHelper;
import com.t4f.lc_helper.utils.Tools;


public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SQLiteDatabase db;
    private Cursor cursor;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 设置 actionbar
        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);

        // 启用向上导航
        setHomeAsUp(toolbar);

        // 读取历史记录
        SQLiteOpenHelper dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_history);

        // 生成 cursor，为 recyclerView 构建 adapter
        try {
            db = dbHelper.getReadableDatabase();
            cursor = getCursor();

            myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, cursor);
            recyclerView.setAdapter(myRecyclerViewAdapter);

        } catch (SQLiteException e) {
            String message = "HistoryActivity: 找不到历史记录";
            Toast toast = Toast.makeText(this, message,
                    Toast.LENGTH_LONG);
            toast.show();
        }

        // 设置 recyclerView 为 LinearLayout
        setLinearLayout();

        // 设置 recyclerView 的点击响应动作
        setOnItemClickListener();
    }

    private void setHomeAsUp(Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(HistoryActivity.this);
                }
            });
        }
    }

    private void setLinearLayout() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setOnItemClickListener() {
        // 添加历史记录的链接
        if (myRecyclerViewAdapter != null) {
            myRecyclerViewAdapter.setListener(new MyRecyclerViewAdapter.Listener() {
                @Override
                public void onClick(String title) {
                    Intent intent = new Intent(HistoryActivity.this,
                            SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.STR_TITLE, title.trim());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_history_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete) {
            clearHistory();
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearHistory() {
        String CLEAR_HISTORY = "DELETE FROM " + DatabaseHelper.TABLE_HISTORY;
        db.execSQL(CLEAR_HISTORY);

        refresh();
    }

    private void refresh() {
        cursor.close();

        Cursor newCursor = getCursor();
        MyRecyclerViewAdapter newAdapter = new MyRecyclerViewAdapter(this, newCursor);
        recyclerView.swapAdapter(newAdapter, false);

        myRecyclerViewAdapter = newAdapter;
        cursor = newCursor;
    }

    private Cursor getCursor() {
        return db.query(DatabaseHelper.TABLE_HISTORY,
                new String[]{"title", "date"},
                null,
                null,
                null,
                null,
                DatabaseHelper.KEY_HISTORY_DATE + " DESC",
                null); //  + " DESC"
    }

    @Override
    public void onRestart() {
        super.onRestart();

        refresh();
        setOnItemClickListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
        if (db != null) db.close();
    }
}
