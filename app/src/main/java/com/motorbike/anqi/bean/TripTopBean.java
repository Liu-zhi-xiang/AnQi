package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/1/31.
 */

public class TripTopBean {
    public  int type;
    private MonthTopListBean monthTopListBean;
    private WeekTopListBean weekTopListBean;

    public TripTopBean(int type, MonthTopListBean monthTopListBean, WeekTopListBean weekTopListBean) {
        this.type = type;
        this.monthTopListBean = monthTopListBean;
        this.weekTopListBean = weekTopListBean;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MonthTopListBean getTopThreeBean() {
        return monthTopListBean;
    }

    public void setTopThreeBean(MonthTopListBean monthTopListBean) {
        this.monthTopListBean = monthTopListBean;
    }

    public WeekTopListBean getTopOtherBean() {
        return weekTopListBean;
    }

    public void setTopOtherBean(WeekTopListBean weekTopListBean) {
        this.weekTopListBean = weekTopListBean;
    }
}
