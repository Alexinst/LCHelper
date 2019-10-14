package com.t4f.lc_helper.utils;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class JsonDataReader {

    public Map<String, Info> readJsonStream(InputStream in) throws IOException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readCmdInfoArray(jsonReader);
        }
        finally {
            jsonReader.close();
        }
    }

    public Map<String, Info> readCmdInfoArray(JsonReader reader) throws IOException{
        Map<String, Info> infos = new HashMap<>();

        reader.beginArray();
        while (reader.hasNext()) {
            Info info = readInfo(reader);
            infos.put(info.getTitle(), info);
        }
        reader.endArray();

        return infos;
    }

    public Info readInfo(JsonReader reader) throws IOException {
        String cmdName = null;
        String p = null;
        String intro = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String tag = reader.nextName();
            switch (tag) {
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
