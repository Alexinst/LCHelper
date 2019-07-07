package com.t4f.lc_helper;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

class Info {
    private String name;
    private String p;
    private String intro;

    public Info(String name, String p, String intro) {
        this.name = name;
        this.p = p;
        this.intro = intro;
    }

    public String getName() {
        return this.name;
    }

    public String getP() {
        return this.p;
    }

    public String getIntro() {
        return this.intro;
    }
}

class CmdDataReader {
    public List<Info> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readCmdInfoArray(reader);
        }
        finally {
            reader.close();
        }
    }

    private List<Info> readCmdInfoArray(JsonReader reader) throws IOException{
        List<Info> infos = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext())
            infos.add(readInfo(reader));
        reader.endArray();

        return infos;
    }

    private Info readInfo(JsonReader reader) throws IOException {
        String cmdName = null;
        String p = null;
        String intro = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "n": cmdName = reader.nextString(); break;
                case "p": p = reader.nextString(); break;
                case "d": intro = reader.nextString(); break;
                default: reader.skipValue();
            }
        }
        reader.endObject();

        return new Info(cmdName, p, intro);
    }
}
