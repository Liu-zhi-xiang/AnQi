package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class OtherInfoBean {

    /**
     * userId : null
     * nickname : 王五
     * area : 杭州
     * headerImg : http://172.21.59.2:8080/img/user/avatar/2dc23cb0-41a9-42b9-aea5-7a7945539083.png
     * sex : 1
     * age : 15
     * level : Lv2
     * carType : null
     * totalMileage : 100.0
     * birthday : null
     * attention : 0
     * pointNum : 41
     * noteList : [{"postId":"2","title":"yu8wuu3et","status":"","content":"578i","publishTime":1521771998000,"publishTimeStr":null,"createBy":"","createTime":"","nickname":"","name":"","headerImg":"","zan":"2","talk":"6","city":"","noteImgList":[],"noteBackImg":"","iszan":""}]
     * tripList : [{"startTime":"2018-03-20 14:02:37.0","ridingTime":"12","ridingKm":"47","avgSpeed":"74","extreme":"410","accelerate100km":"74","bend":"45"}]
     * phone : null
     */

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
    /**
     * postId : 2
     * title : yu8wuu3et
     * status :
     * content : 578i
     * publishTime : 1521771998000
     * publishTimeStr : null
     * createBy :
     * createTime :
     * nickname :
     * name :
     * headerImg :
     * zan : 2
     * talk : 6
     * city :
     * noteImgList : []
     * noteBackImg :
     * iszan :
     */

    private List<NoteListBean> noteList;
    /**
     * startTime : 2018-03-20 14:02:37.0
     * ridingTime : 12
     * ridingKm : 47
     * avgSpeed : 74
     * extreme : 410
     * accelerate100km : 74
     * bend : 45
     */

    private List<TripListBean> tripList;

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

    public List<NoteListBean> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<NoteListBean> noteList) {
        this.noteList = noteList;
    }

    public List<TripListBean> getTripList() {
        return tripList;
    }

    public void setTripList(List<TripListBean> tripList) {
        this.tripList = tripList;
    }


}
