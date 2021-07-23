package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class RoomUserBean {


    /**
     * userList : [{"userId":"1017","nickname":"tianyu","headerImg":"http://192.168.191.2:8080/img","isAdmin":"0","microphone":"1"},{"userId":"1007","nickname":"赵六","headerImg":"http://192.168.191.2:8080/img/user/avatar/006.png","isAdmin":"0","microphone":"1"},{"userId":"1012","nickname":"ryu","headerImg":"http://192.168.191.2:8080/img/user/avatar/b91f9ae7-59c1-474a-b617-e3a0a417a9b9.jpg","isAdmin":"0","microphone":"1"}]
     * leaderId : 1017
     * aqRoomInfoStr : null
     */

    private String leaderId;
    private String aqRoomInfoStr;
    /**
     * userId : 1017
     * nickname : tianyu
     * headerImg : http://192.168.191.2:8080/img
     * isAdmin : 0
     * microphone : 1
     */

    private List<UserListBean> userList;
    /**
     * micrType : 1
     */

    private String micrType;

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getAqRoomInfoStr() {
        return aqRoomInfoStr;
    }

    public void setAqRoomInfoStr(String aqRoomInfoStr) {
        this.aqRoomInfoStr = aqRoomInfoStr;
    }

    public List<UserListBean> getUserList() {
        return userList;
    }

    public void setUserList(List<UserListBean> userList) {
        this.userList = userList;
    }


    public String getMicrType() {
        return micrType;
    }

    public void setMicrType(String micrType) {
        this.micrType = micrType;
    }
}
