package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/23.
 */

public class UserCarBean {

    /**
     * id : 23
     * userId : 1012
     * brand : 宝马
     * models : A350
     * status : 0
     * idStr : 23
     * userIdStr : 1012
     *
     * {,"isdefault":null,"statusStr":"1","idStr":"98","}
     */

    private int id;
    private int userId;
    private String brand;
    private String models;
    private int status;
    private String idStr;
    private String userIdStr;
    private String isdefault;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }
}
