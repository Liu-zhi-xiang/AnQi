package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/5/18.
 */

public class RecordBean {

    /**
     * pointType : 0
     * reason : null
     * createTime : 2018-05-18
     * pointNum : 4.0
     */

    private String pointType;
    private Object reason;
    private String createTime;
    private String pointNum;

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPointNum() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
        this.pointNum = pointNum;
    }
}
