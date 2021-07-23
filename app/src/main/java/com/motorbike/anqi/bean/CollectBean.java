package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/24.
 */

public class CollectBean {

    /**
     * roomId : 1
     * roomNo :
     * name :
     * theme : 滴水湖之旅
     * headerImg :
     * backUrl : http://172.21.59.2:8080/img/room/coverImg/test1.png
     * status :
     */

    private String roomId;
    private String roomNo;
    private String name;
    private String theme;
    private String headerImg;
    private String backUrl;
    private String status;
    private String isPassWord;
    private String password;

    public String getIsPassWord() {
        return isPassWord;
    }

    public void setIsPassWord(String isPassWord) {
        this.isPassWord = isPassWord;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
