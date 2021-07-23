package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class TripBean {

    /**
     * ownTripList : [{"ridingKm":"45","startAddress":"五角场","endAddress":"滴水湖","startTime":"2018-03-05 23:56:43.0","roomId":"1","id":1,"tripId":"1"},{"ridingKm":"42","startAddress":"宝山","endAddress":"徐汇","startTime":"2018-03-21 14:00:28.0","roomId":"2","id":2,"tripId":"2"}]
     * teamTripList : null
     */

    private List<TeamTripBean> teamTripList;
    /**
     * ridingKm : 45
     * startAddress : 五角场
     * endAddress : 滴水湖
     * startTime : 2018-03-05 23:56:43.0
     * roomId : 1
     * id : 1
     * tripId : 1
     */

    private List<OwnTripListBean> ownTripList;

    public List<TeamTripBean> getTeamTripList() {
        return teamTripList;
    }

    public void setTeamTripList(List<TeamTripBean> teamTripList) {
        this.teamTripList = teamTripList;
    }

    public List<OwnTripListBean> getOwnTripList() {
        return ownTripList;
    }

    public void setOwnTripList(List<OwnTripListBean> ownTripList) {
        this.ownTripList = ownTripList;
    }


}
