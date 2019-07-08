package com.t4f.lc_helper.data;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonDataReaderTest {
    JsonDataReader reader = new JsonDataReader();

    @Test
    public void testReadJsonStream() {
        List<Info> test = new ArrayList<>();
        test.add(new Info("ab","/ab", "Apache服务器的性能测试工具"));
        test.add(new Info("accept","/accept","指示打印系统接受发往指定目标打印机的打印任务"));
        test.add(new Info("ack","/ack","比grep好用的文本搜索工具"));

        String filename = "e:/Projects/data4test.json";
        List<Info> cmdLists = null;
        try {
            InputStream in = new FileInputStream(filename);
            cmdLists = reader.readJsonStream(in);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(cmdLists.get(0).getName(), test.get(0).getName());
    }
}