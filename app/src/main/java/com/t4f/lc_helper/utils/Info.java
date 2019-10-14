package com.t4f.lc_helper.utils;

public class Info implements java.io.Serializable {
    private String title;
    private String date;
    private String p;
    private String intro;

    public Info(String title) {
        setTitle(title);
        p = "";
        intro = "";
    }

    public Info(String title, String date) {
        setTitle(title);
        this.date = date;
    }

    public Info(String title, String p, String intro) {
        setTitle(title);
        setP(p);
        setIntro(intro);
    }

    public Info(String title, String p, String intro, String date) {
        setTitle(title);
        setP(p);
        setIntro(intro);
        setDate(date);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return this.date.toString();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getP() {
        return this.p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}