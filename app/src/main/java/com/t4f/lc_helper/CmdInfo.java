package com.t4f.lc_helper;

import java.util.ArrayList;
import java.util.List;

public class CmdInfo {
    public String name;
    public List<Info> commands = new ArrayList<>();
}

class Info {
    public String name;

    public String p;

    public String intro;
}
