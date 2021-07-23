package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/1/30.
 * 私信bean
 */

public class PrivateLeterBean {
    private String name;
    private String content;
    private String date;
    private String image;

    public PrivateLeterBean(String name, String content, String date, String image) {
        this.name = name;
        this.content = content;
        this.date = date;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
