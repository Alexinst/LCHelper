package com.t4f.lc_helper.data;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class JsonDataReader {

    public List<Info> readJsonStream(InputStream in) throws IOException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readCmdInfoArray(jsonReader);
        }
        finally {
            jsonReader.close();
        }
    }

    public List<Info> readCmdInfoArray(JsonReader reader) throws IOException{
        List<Info> infos = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext())
            infos.add(readInfo(reader));
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
