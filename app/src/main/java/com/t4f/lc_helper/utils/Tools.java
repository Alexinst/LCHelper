package com.t4f.lc_helper.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.t4f.lc_helper.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Tools {
    public static final String language = "zh";

    public static Map<String, Info> readData(Context context, String filename) {
//        String filename = "CmdInfo.json";
        JsonDataReader dataReader = new JsonDataReader();
        Map<String, Info> cmds = null;

        try {
            InputStream in = context.getResources().getAssets().open(filename);
            cmds = dataReader.readJsonStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cmds;
    }

    public static void setHomeAsUp(final Activity activity, Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(activity);
                }
            });
        }
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                new Locale(language));

        Date now = new Date();  // 当前时间

        return sdf.format(now);
    }
}
