package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class OwnTripDetaiBean {

    /**
     * nickname : ryu
     * headerImg : http://172.17.19.3:8080/img/user/avatar/007.png
     * setTime : null
     * city : 上海
     * carType : null
     * startTime : 2018-03-05 23:56:43
     * ridingTime : 26
     * ridingKm : 45.0
     */

    private TripInfoStrBean tripInfoStr;
    /**
     * avgSpeed : 56.0
     * extreme : 667.0
     * accelerate100km : 66.0
     * headerImg :
     * bend : 66.0
     */

    private List<RidingDataListBean> ridingDataList;

    public TripInfoStrBean getTripInfoStr() {
        return tripInfoStr;
    }

    public void setTripInfoStr(TripInfoStrBean tripInfoStr) {
        this.tripInfoStr = tripInfoStr;
    }

    public List<RidingDataListBean> getRidingDataList() {
        return ridingDataList;
    }

    public void setRidingDataList(List<RidingDataListBean> ridingDataList) {
        this.ridingDataList = ridingDataList;
    }

}
