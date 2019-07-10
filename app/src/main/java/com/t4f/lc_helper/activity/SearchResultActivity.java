package com.t4f.lc_helper.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.t4f.lc_helper.R;

import java.io.FileNotFoundException;
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private String readText(InputStream is) throws Exception {
//        InputStreamReader reader = new InputStreamReader(is);
//        BufferedReader bufferedReader = new BufferedReader(reader);
//        StringBuilder sb = new StringBuilder("");
//        String str;
//        while ((str = bufferedReader.readLine()) != null) {
//            sb.append(str);
//            sb.append("\n");
//        }
//        return sb.toString();
//    }
}
