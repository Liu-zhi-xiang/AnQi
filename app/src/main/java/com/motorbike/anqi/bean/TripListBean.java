package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/24.
 */

public class TripListBean {
    private String startTime;
    private String ridingTime;
    private String ridingKm;
    private String avgSpeed;
    private String extreme;
    private String accelerate100km;
    private String bend;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRidingTime() {
        return ridingTime;
    }

    public void setRidingTime(String ridingTime) {
        this.ridingTime = ridingTime;
    }

    public String getRidingKm() {
        return ridingKm;
    }

    public void setRidingKm(String ridingKm) {
        this.ridingKm = ridingKm;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getExtreme() {
        return extreme;
    }

    public void setExtreme(String extreme) {
        this.extreme = extreme;
    }

    public String getAccelerate100km() {
        return accelerate100km;
    }

    public void setAccelerate100km(String accelerate100km) {
        this.accelerate100km = accelerate100km;
    }

    public String getBend() {
        return bend;
    }

    public void setBend(String bend) {
        this.bend = bend;
    }
}
