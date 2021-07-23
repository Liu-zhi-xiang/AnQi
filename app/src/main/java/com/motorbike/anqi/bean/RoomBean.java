package com.motorbike.anqi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/24.
 */

public class RoomBean implements Parcelable {


    /**
     * userList : null
     * leaderId : null
     * micrType : null
     * aqRoomInfoStr : {"nickname":"lzx","headerImg":"http://118.31.9.86:80/img/user/avatar/3e25af0f-2c5a-4e96-8bc5-3703625d5382.jpg","leaderId":"1023","type":"2","theme":"Tjjjkkk","startAddress":"上海市徐汇区钦州北路1021号","endAddress":"上海市徐汇区钦州北路1006","setTime":"2018-11-27 16:04","setAddress":"上海市徐汇区钦州北路1006","remark":"Fjjjjjkk","personTotal":"4","onLineCount":"2","backUrl":"","longitudeStart":"121.414498","latitudeStart":"31.185488","longitudeDestination":"121.41621318739801","latitudeDestination":"31.186291949105243","longitudeGather":"121.414498","latitudeGather":"31.185488"}
     */

    private String userList;
    private String leaderId;
    private String micrType;
    /**
     * nickname : lzx
     * headerImg : http://118.31.9.86:80/img/user/avatar/3e25af0f-2c5a-4e96-8bc5-3703625d5382.jpg
     * leaderId : 1023
     * type : 2
     * theme : Tjjjkkk
     * startAddress : 上海市徐汇区钦州北路1021号
     * endAddress : 上海市徐汇区钦州北路1006
     * setTime : 2018-11-27 16:04
     * setAddress : 上海市徐汇区钦州北路1006
     * remark : Fjjjjjkk
     * personTotal : 4
     * onLineCount : 2
     * backUrl :
     * longitudeStart : 121.414498
     * latitudeStart : 31.185488
     * longitudeDestination : 121.41621318739801
     * latitudeDestination : 31.186291949105243
     * longitudeGather : 121.414498
     * latitudeGather : 31.185488
     */

    private AqRoomInfoStrBean aqRoomInfoStr;

    public String getUserList() {
        return userList;
    }

    public void setUserList(String userList) {
        this.userList = userList;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getMicrType() {
        return micrType;
    }

    public void setMicrType(String micrType) {
        this.micrType = micrType;
    }

    public AqRoomInfoStrBean getAqRoomInfoStr() {
        return aqRoomInfoStr;
    }

    public void setAqRoomInfoStr(AqRoomInfoStrBean aqRoomInfoStr) {
        this.aqRoomInfoStr = aqRoomInfoStr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userList);
        dest.writeString(this.leaderId);
        dest.writeString(this.micrType);
        dest.writeParcelable(this.aqRoomInfoStr, flags);
    }

    public RoomBean() {
    }

    protected RoomBean(Parcel in) {
        this.userList = in.readString();
        this.leaderId = in.readString();
        this.micrType = in.readString();
        this.aqRoomInfoStr = in.readParcelable(AqRoomInfoStrBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<RoomBean> CREATOR = new Parcelable.Creator<RoomBean>() {
        @Override
        public RoomBean createFromParcel(Parcel source) {
            return new RoomBean(source);
        }

        @Override
        public RoomBean[] newArray(int size) {
            return new RoomBean[size];
        }
    };
}
