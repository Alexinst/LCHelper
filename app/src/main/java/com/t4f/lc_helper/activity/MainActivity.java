package com.t4f.lc_helper.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.t4f.lc_helper.data.Info;
import com.t4f.lc_helper.data.JsonDataReader;
import com.t4f.lc_helper.R;
import com.t4f.lc_helper.data.Trie;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AutoCompleteTextView inputBox;
    public Map<String, Info> cmdMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // 读取data.json
        String dataFiles = "CmdInfo.json";
        JsonDataReader dataReader = new JsonDataReader();

        try {
            InputStream in = getResources().getAssets().open(dataFiles);
            cmdMap = dataReader.readJsonStream(in);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // ToTest: 创建前缀树，储存所有指令名
        Trie cmdTree = new Trie();
        for (String cmdName : cmdMap.keySet()) {
            cmdTree.insert(cmdName);
        }

        // TODO:监听 AutoCompleteTextView
        inputBox = (AutoCompleteTextView) findViewById(R.id.input_box);
        // 设置数据源

        String[] autoStrings = new String[cmdMap.keySet().size()];
        int i = 0;
        for (String cmdName : cmdMap.keySet()) {
            autoStrings[i] = cmdName;
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, autoStrings);
        inputBox.setAdapter(adapter);

//        inputBox.addTextChangedListener(new TextWatcher() {
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
//                suggessions.add(cmdMap.get(str).getName());
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
        TextView searchBox = (TextView) findViewById(R.id.input_box);
        String cmd = searchBox.getText().toString().toLowerCase();

        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("cmd", cmd);
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

        } else if (id == R.id.nav_slideshow) {

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
