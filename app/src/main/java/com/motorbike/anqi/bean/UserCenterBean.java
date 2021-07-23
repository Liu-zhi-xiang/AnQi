package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/18.
 */

public class UserCenterBean {

    /**
     * userId : 1012
     * nickname : cuiguanfeng
     * area : 上海
     * headerImg : http://192.168.43.37:8080/img/user/avatar/u2.png
     * sex : 1
     * age : null
     * carType : null
     * totalMileage : 50.0
     * attention : null
     * pointNum : 40
     * noteList : null
     * tripList : null
     */

    private String userId;
    private String nickname;
    private String area;
    private String headerImg;
    private String sex;
    private String age;
    private String carType;
    private String totalMileage;
    private String attention;
    private String pointNum;
    private Object noteList;
    private Object tripList;
    private String level;
    private String birthday;
    private String phone;
    private String brand;
    private String models;
    private String deliverAddress;
    private String isConfirm;

    public String getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(String isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Object getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(String totalMileage) {
        this.totalMileage = totalMileage;
    }

    public Object getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getPointNum() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
        this.pointNum = pointNum;
    }

    public Object getNoteList() {
        return noteList;
    }

    public void setNoteList(Object noteList) {
        this.noteList = noteList;
    }

    public Object getTripList() {
        return tripList;
    }

    public void setTripList(Object tripList) {
        this.tripList = tripList;
    }
}
