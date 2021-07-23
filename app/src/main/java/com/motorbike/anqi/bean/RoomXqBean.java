package com.motorbike.anqi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lzx
 * @date 2018/5/15
 * @info
 */

public class RoomXqBean implements Parcelable {

    /**
     * nickname : lzx
     * headerImg : http://172.20.10.2:8080/img/user/avatar/5e13b1ec-3eef-4ae3-a25b-d5f32a876a7a.png
     * leaderId : null
     * type : 2
     * theme : gloomy
     * startAddress :
     * endAddress :
     * setTime : 2018-05-09 19:10
     * setAddress :
     * remark : 测试房间
     * personTotal : 4
     * onLineCount : 1
     * backUrl : http://172.20.10.2:8080/img/room/coverImg/f0762a8a-0837-4aab-9817-b671da2f334e.jpg
     * longitudeStart :
     * latitudeStart :
     * longitudeDestination :
     * latitudeDestination :
     * longitudeGather :
     * latitudeGather :
     */

    private String nickname;
    private String headerImg;
    private String leaderId;
    private String type;
    private String theme;
    private String startAddress;
    private String endAddress;
    private String setTime;
    private String setAddress;
    private String remark;
    private String personTotal;
    private String onLineCount;
    private String backUrl;
    private String longitudeStart;
    private String latitudeStart;
    private String longitudeDestination;
    private String latitudeDestination;
    private String longitudeGather;
    private String latitudeGather;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public String getSetAddress() {
        return setAddress;
    }

    public void setSetAddress(String setAddress) {
        this.setAddress = setAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPersonTotal() {
        return personTotal;
    }

    public void setPersonTotal(String personTotal) {
        this.personTotal = personTotal;
    }

    public String getOnLineCount() {
        return onLineCount;
    }

    public void setOnLineCount(String onLineCount) {
        this.onLineCount = onLineCount;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getLongitudeStart() {
        return longitudeStart;
    }

    public void setLongitudeStart(String longitudeStart) {
        this.longitudeStart = longitudeStart;
    }

    public String getLatitudeStart() {
        return latitudeStart;
    }

    public void setLatitudeStart(String latitudeStart) {
        this.latitudeStart = latitudeStart;
    }

    public String getLongitudeDestination() {
        return longitudeDestination;
    }

    public void setLongitudeDestination(String longitudeDestination) {
        this.longitudeDestination = longitudeDestination;
    }

    public String getLatitudeDestination() {
        return latitudeDestination;
    }

    public void setLatitudeDestination(String latitudeDestination) {
        this.latitudeDestination = latitudeDestination;
    }

    public String getLongitudeGather() {
        return longitudeGather;
    }

    public void setLongitudeGather(String longitudeGather) {
        this.longitudeGather = longitudeGather;
    }

    public String getLatitudeGather() {
        return latitudeGather;
    }

    public void setLatitudeGather(String latitudeGather) {
        this.latitudeGather = latitudeGather;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeString(this.headerImg);
        dest.writeString(this.leaderId);
        dest.writeString(this.type);
        dest.writeString(this.theme);
        dest.writeString(this.startAddress);
        dest.writeString(this.endAddress);
        dest.writeString(this.setTime);
        dest.writeString(this.setAddress);
        dest.writeString(this.remark);
        dest.writeString(this.personTotal);
        dest.writeString(this.onLineCount);
        dest.writeString(this.backUrl);
        dest.writeString(this.longitudeStart);
        dest.writeString(this.latitudeStart);
        dest.writeString(this.longitudeDestination);
        dest.writeString(this.latitudeDestination);
        dest.writeString(this.longitudeGather);
        dest.writeString(this.latitudeGather);
    }

    public RoomXqBean() {
    }

    protected RoomXqBean(Parcel in) {
        this.nickname = in.readString();
        this.headerImg = in.readString();
        this.leaderId = in.readString();
        this.type = in.readString();
        this.theme = in.readString();
        this.startAddress = in.readString();
        this.endAddress = in.readString();
        this.setTime = in.readString();
        this.setAddress = in.readString();
        this.remark = in.readString();
        this.personTotal = in.readString();
        this.onLineCount = in.readString();
        this.backUrl = in.readString();
        this.longitudeStart = in.readString();
        this.latitudeStart = in.readString();
        this.longitudeDestination = in.readString();
        this.latitudeDestination = in.readString();
        this.longitudeGather = in.readString();
        this.latitudeGather = in.readString();
    }

    public static final Parcelable.Creator<RoomXqBean> CREATOR = new Parcelable.Creator<RoomXqBean>() {
        @Override
        public RoomXqBean createFromParcel(Parcel source) {
            return new RoomXqBean(source);
        }

        @Override
        public RoomXqBean[] newArray(int size) {
            return new RoomXqBean[size];
        }
    };
}
