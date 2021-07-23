package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/26.
 */

public class OtherDetailBean {
    private String userId;
    private String nickname;
    private String area;
    private String headerImg;
    private String sex;
    private String age;
    private String level;
    private String carType;
    private String totalMileage;
    private String birthday;
    private String attention;
    private String pointNum;
    private String phone;

    public OtherDetailBean(String userId, String nickname, String area, String headerImg, String sex, String age, String level, String carType, String totalMileage, String birthday, String attention, String pointNum, String phone) {
        this.userId = userId;
        this.nickname = nickname;
        this.area = area;
        this.headerImg = headerImg;
        this.sex = sex;
        this.age = age;
        this.level = level;
        this.carType = carType;
        this.totalMileage = totalMileage;
        this.birthday = birthday;
        this.attention = attention;
        this.pointNum = pointNum;
        this.phone = phone;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAttention() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
