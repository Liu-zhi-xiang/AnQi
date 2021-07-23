package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */

public class TripDetailBean {
    public  int type;
    private TripInfoStrBean topThreeBean;
    private List<RidingDataListBean> topOtherBean;

    public TripDetailBean(int type, TripInfoStrBean topThreeBean, List<RidingDataListBean> topOtherBean) {
        this.type = type;
        this.topThreeBean = topThreeBean;
        this.topOtherBean = topOtherBean;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TripInfoStrBean getTopThreeBean() {
        return topThreeBean;
    }

    public void setTopThreeBean(TripInfoStrBean topThreeBean) {
        this.topThreeBean = topThreeBean;
    }

    public List<RidingDataListBean> getTopOtherBean() {
        return topOtherBean;
    }

    public void setTopOtherBean(List<RidingDataListBean> topOtherBean) {
        this.topOtherBean = topOtherBean;
    }
}
