package com.t4f.lc_helper.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.t4f.lc_helper.R;
import com.t4f.lc_helper.adapter.MyRecyclerViewAdapter;
import com.t4f.lc_helper.sql.DatabaseHelper;
import com.t4f.lc_helper.utils.Tools;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerAll;
    private MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        // 设置 action bar
        Toolbar toolbar = findViewById(R.id.toolbar_view_all);
        setSupportActionBar(toolbar);

        // 启用向上导航
        Tools.setHomeAsUp(this, toolbar);
//        setHomeAsUp(toolbar);

        recyclerAll = findViewById(R.id.recycler_all);
        setAdapter();


        // 设置 LinearLayout
        setLinearLayout();

//        // 设置 recyclerAll 的响应事件
        setOnItemClickListener();
    }

    private void setAdapter() {
        SQLiteOpenHelper dbHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

//        String SELECT_ALL = String.format("SELECT * FROM %s", DatabaseHelper.TABLE_COMMAND);
        Cursor cursor = db.query(DatabaseHelper.TABLE_COMMAND,
                new String[]{DatabaseHelper.KEY_COMMAND_TITLE},
                null,
                null,
                null,
                null,
                DatabaseHelper.KEY_COMMAND_TITLE,
                null
        );
        adapter = new MyRecyclerViewAdapter(this, cursor);
//        db.close();

        recyclerAll.setAdapter(adapter);
    }

    private void setLinearLayout() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerAll.setLayoutManager(linearLayout);
    }

    private void setOnItemClickListener() {
        if (adapter != null) {
            adapter.setListener(new MyRecyclerViewAdapter.Listener() {
                @Override
                public void onClick(String title) {
                    Intent intent = new Intent(ViewAllActivity.this,
                            SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.STR_TITLE, title.trim());
                    startActivity(intent);
                }
            });
        }
    }
}
