package com.t4f.lc_helper.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.t4f.lc_helper.R;
import com.t4f.lc_helper.sql.DatabaseHelper;
import com.t4f.lc_helper.utils.Tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class SearchResultActivity extends AppCompatActivity {

    public static final String STR_TITLE = "cmd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolbar = findViewById(R.id.toolbar_search_result);
        setSupportActionBar(toolbar);

        // 启用向上导航
        setHomeAsUp(toolbar);

        Intent intent = getIntent();
        String cmd = intent.getStringExtra(STR_TITLE);
        String filename = String.format("commands/%s.md", cmd);
        renderMarkdown(filename);

        // 更新表单 History
        new UpdateDataBaseTask().execute(cmd);
    }

    private void setHomeAsUp(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle(R.string.label_search_result);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(SearchResultActivity.this);
            }
        });
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class UpdateDataBaseTask extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... cmdNames) {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(SearchResultActivity.this);

            long id = dbHelper.addOrUpdateHistory(cmdNames[0], Tools.getTime());

            return id != -1;
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
