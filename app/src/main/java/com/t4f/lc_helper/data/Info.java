package com.t4f.lc_helper.data;

public class Info {
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