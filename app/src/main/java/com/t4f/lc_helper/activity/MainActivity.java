package com.t4f.lc_helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.t4f.lc_helper.data.Info;
import com.t4f.lc_helper.data.JsonDataReader;
import com.t4f.lc_helper.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AutoCompleteTextView searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        runAtFirst();

        // 读取data.json
        Map<String, Info> cmds = readData();

//        // ToTest: 创建前缀树，储存所有指令名
//        Trie cmdTree = new Trie();
//        for (String cmdName : cmds.keySet()) {
//            cmdTree.insert(cmdName);
//        }

        // 设置 AutoCompleteTextView 数据源
        searchBox = (AutoCompleteTextView) findViewById(R.id.input_box);
        String[] autoStrings = cmds.keySet().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, autoStrings);
        searchBox.setAdapter(adapter);
    }

    private void runAtFirst() {
        SharedPreferences sp = getSharedPreferences("isFirst",
                                                    Context.MODE_PRIVATE);
        Boolean isFirst = sp.getBoolean("isFirst", true);
        if (isFirst) {
            Toast.makeText(this, "First Run", Toast.LENGTH_LONG).show();
            sp.edit().putBoolean("isFirst", false).commit();
        } else
            Toast.makeText(this, "Not First Run", Toast.LENGTH_LONG).show();
    }

    private Map<String, Info> readData() {
        String dataFiles = "CmdInfo.json";
        JsonDataReader dataReader = new JsonDataReader();
        Map<String, Info> cmds = null;

        try {
            InputStream in = getResources().getAssets().open(dataFiles);
            cmds = dataReader.readJsonStream(in);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return cmds;
    }

    private void topK() {
//        searchBox.addTextChangedListener(new TextWatcher() {
////            private CharSequence temp;
////            private int selectionStart;
////            private int selectionEnd;
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                StringBuilder str = new StringBuilder();
//                for (int i = 0; i < s.length(); i++) {
//                    char c = s.charAt(i);
//                    if (   (c - 'a' >= 0 && c - 'a' < 26)
//                        || (c - '0' >= 0 && c - '0' < 10)
//                        || c == '-'
//                        || c == '_')
//                        str.append(c);
//                }
//
//                List<String> suggessions = new ArrayList<>();
//                suggessions.add(cmds.get(str).getTitle());
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_list_item_1, suggessions);
//                suggessionsPop.setAdapter(adapter);
//
//
//                // Top K algorithm
//            }
//        });
    }

    public void onClickSearch(View view) {
        // 获取待查询命令名
        String cmdName = searchBox.getText().toString().toLowerCase().trim();

        // 读取并显示对应命令详情
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("cmd", cmdName);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

        }
//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
