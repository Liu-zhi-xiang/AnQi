package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/26.
 */

public class OwnTripListBean {

    private String ridingKm;
    private String startAddress;
    private String endAddress;
    private String startTime;
    private String roomId;
    private int id;
    private String tripId;

    public String getRidingKm() {
        return ridingKm;
    }

    public void setRidingKm(String ridingKm) {
        this.ridingKm = ridingKm;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
