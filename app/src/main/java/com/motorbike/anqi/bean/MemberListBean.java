package com.motorbike.anqi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/24.
 */

public class MemberListBean implements Parcelable {

    /**
     * nickname : 王五
     * area : 杭州
     * headerImg : http://172.21.59.2:8080/img/user/avatar/2dc23cb0-41a9-42b9-aea5-7a7945539083.png
     * friendId : 1001
     * isAdmin : 1
     * isForbid : 0
     */

    private String nickname;
    private String area;
    private String headerImg;
    private String friendId;
    private String isAdmin;
    private String isForbid;
    private String isAttention;
    private String isLeader;
    /**
     * longitude : 121.412493
     * latitude : 31.185442
     */

    private String longitude;
    private String latitude;

    public String getIsAttention() {
        return isAttention == null ? "" : isAttention;
    }

    public MemberListBean setIsAttention(String isAttention) {
        this.isAttention = isAttention;
        return this;
    }

    public String getIsLeader() {
        return isLeader == null ? "" : isLeader;
    }

    public MemberListBean setIsLeader(String isLeader) {
        this.isLeader = isLeader;
        return this;
    }

    public static Creator<MemberListBean> getCREATOR() {
        return CREATOR;
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

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsForbid() {
        return isForbid;
    }

    public void setIsForbid(String isForbid) {
        this.isForbid = isForbid;
    }

    public MemberListBean() {
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeString(this.area);
        dest.writeString(this.headerImg);
        dest.writeString(this.friendId);
        dest.writeString(this.isAdmin);
        dest.writeString(this.isForbid);
        dest.writeString(this.isAttention);
        dest.writeString(this.isLeader);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
    }

    protected MemberListBean(Parcel in) {
        this.nickname = in.readString();
        this.area = in.readString();
        this.headerImg = in.readString();
        this.friendId = in.readString();
        this.isAdmin = in.readString();
        this.isForbid = in.readString();
        this.isAttention = in.readString();
        this.isLeader = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
    }

    public static final Creator<MemberListBean> CREATOR = new Creator<MemberListBean>() {
        @Override
        public MemberListBean createFromParcel(Parcel source) {
            return new MemberListBean(source);
        }

        @Override
        public MemberListBean[] newArray(int size) {
            return new MemberListBean[size];
        }
    };
}
