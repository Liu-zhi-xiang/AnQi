package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class IndexBean {

    /**
     * friendRoomList : null
     * recommendRoomList : [{"roomId":"2","roomNo":"002","name":"zhaoliu","theme":"宝山一日游","headerImg":"http://172.20.134.2:8080/img/user/avatar/006.png","backUrl":"http://172.20.134.2:8080/img/room/coverImg/test.png","status":"0"},{"roomId":"1","roomNo":"001","name":"zhangsan","theme":"滴水湖之旅","headerImg":"http://172.20.134.2:8080/img/user/avatar/004.png","backUrl":"http://172.20.134.2:8080/img/room/coverImg/test1.png","status":"1"}]
     * messageCount : 2
     */

    private List<RecommendRoomListBean> friendRoomList;
    private String messageCount;
    /**
     * roomId : 2
     * roomNo : 002
     * name : zhaoliu
     * theme : 宝山一日游
     * headerImg : http://172.20.134.2:8080/img/user/avatar/006.png
     * backUrl : http://172.20.134.2:8080/img/room/coverImg/test.png
     * status : 0
     */

    private List<RecommendRoomListBean> recommendRoomList;
    /**
     * roomStatus : 0
     */

    private String roomStatus;

    public List<RecommendRoomListBean> getFriendRoomList() {
        return friendRoomList;
    }

    public void setFriendRoomList(List<RecommendRoomListBean> friendRoomList) {
        this.friendRoomList = friendRoomList;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public List<RecommendRoomListBean> getRecommendRoomList() {
        return recommendRoomList;
    }

    public void setRecommendRoomList(List<RecommendRoomListBean> recommendRoomList) {
        this.recommendRoomList = recommendRoomList;
    }


    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }
}
