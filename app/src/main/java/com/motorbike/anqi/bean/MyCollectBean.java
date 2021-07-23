package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/1/24.
 */

public class MyCollectBean {
    private String bg;
    private String title;

    public MyCollectBean(String bg, String title) {
        this.bg = bg;
        this.title = title;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
