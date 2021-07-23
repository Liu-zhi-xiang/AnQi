package com.motorbike.anqi.bean;

/**
 * @author lzx
 * @date 2018/4/27
 * @info
 */

public class WeekTopListBean {

    /**
     * nickname : 王五
     * headerImg : http://172.18.82.4:8080/img/user/avatar/2dc23cb0-41a9-42b9-aea5-7a7945539083.png
     * ridingKm : 100
     * rank : null
     * level : 2
     */

    private String nickname;
    private String headerImg;
    private String ridingKm;
    private String rank;
    private String level;

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

    public String getRidingKm() {
        return ridingKm;
    }

    public void setRidingKm(String ridingKm) {
        this.ridingKm = ridingKm;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
