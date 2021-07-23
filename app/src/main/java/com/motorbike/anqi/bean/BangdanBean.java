package com.motorbike.anqi.bean;

import java.util.List;

/**
 * @author lzx
 * @date 2018/4/27
 * @info
 */

public class BangdanBean {

    /**
     * weekTopList : null
     * monthTopList : [{"nickname":"zhagnssang","headerImg":"http://172.18.82.4:8080/img/user/avatar/25898460-a0b4-4b17-a44c-0247f8533e2c.jpg","ridingKm":"100","rank":"1","level":"3"}]
     */

    private List<WeekTopListBean> weekTopList;
    /**
     * nickname : zhagnssang
     * headerImg : http://172.18.82.4:8080/img/user/avatar/25898460-a0b4-4b17-a44c-0247f8533e2c.jpg
     * ridingKm : 100
     * rank : 1
     * level : 3
     */

    private List<MonthTopListBean> monthTopList;

    public List<WeekTopListBean> getWeekTopList() {
        return weekTopList;
    }

    public void setWeekTopList(List<WeekTopListBean> weekTopList) {
        this.weekTopList = weekTopList;
    }

    public List<MonthTopListBean> getMonthTopList() {
        return monthTopList;
    }

    public void setMonthTopList(List<MonthTopListBean> monthTopList) {
        this.monthTopList = monthTopList;
    }


}
