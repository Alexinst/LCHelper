package com.t4f.lc_helper.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.t4f.lc_helper.R;
import com.t4f.lc_helper.sql.DBHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        String cmd = intent.getStringExtra("cmd");
        String filename = String.format("commands/%s.md", cmd);
        renderMarkdown(filename);

        // 更新表单 History
        new UpdateDataBaseTask().execute(cmd);
    }

    private void renderMarkdown(String filename) {
        try {
            InputStream in = getResources().getAssets().open(filename);
            InputStreamReader reader = new InputStreamReader(in);
            Parser parser = Parser.builder().build();
            Node doc = parser.parseReader(reader);
            HtmlRenderer render = HtmlRenderer.builder().build();

            WebView contentBox = (WebView) findViewById(R.id.wv_content);
            contentBox.loadData(render.render(doc), "text/html; charset=UTF-8", null);
            contentBox.setVisibility(View.VISIBLE);

            // WebView自适应屏幕，支持缩放
            WebSettings settings = contentBox.getSettings();
            // settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true); // 自适应屏幕
            settings.setSupportZoom(true);          // 设置支持缩放
            settings.setBuiltInZoomControls(true);  //
            settings.setDisplayZoomControls(false); // 隐藏缩放按钮
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class UpdateDataBaseTask extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... cmds) {
            DBHelper dbHelper =
                    new DBHelper(SearchResultActivity.this);

            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                db.insert(DBHelper.TABLE_NAME, null, cmdValues);
                dbHelper.insertRecord(db, cmds[0]);
                db.close();

                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                String message = "SearchResultActivity: 历史记录更新失败";
                Toast toast = Toast.makeText(SearchResultActivity.this, message,
                                             Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
