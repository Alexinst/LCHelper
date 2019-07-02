package com.t4f.lc_helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

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
        String html = renderMarkdown(filename);

        TextView searchResult = (TextView) findViewById(R.id.search_result);
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

    private String renderMarkdown(String filename) {
        try {
            InputStream in = getResources().getAssets().open(filename);
            InputStreamReader reader = new InputStreamReader(in);
            Parser parser = Parser.builder().build();
            Node doc = parser.parseReader(reader);
            HtmlRenderer render = HtmlRenderer.builder().build();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
