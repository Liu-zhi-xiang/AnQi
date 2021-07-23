package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/18.
 */

public class LevelBean {

    /**
     * nickname : cuiguanfeng
     * headerImg : http://192.168.43.37:8080/img/user/avatar/u2.png
     * currentLevel : Lv3
     * nextLevel : Lv1
     * currentKm : 500.0
     * nextLevelKm : 20.0
     */

    private String nickname;
    private String headerImg;
    private String currentLevel;
    private String nextLevel;
    private String currentKm;
    private String nextLevelKm;
    private String licheng;

    public String getLicheng() {
        return licheng;
    }

    public void setLicheng(String licheng) {
        this.licheng = licheng;
    }

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

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(String nextLevel) {
        this.nextLevel = nextLevel;
    }

    public String getCurrentKm() {
        return currentKm;
    }

    public void setCurrentKm(String currentKm) {
        this.currentKm = currentKm;
    }

    public String getNextLevelKm() {
        return nextLevelKm;
    }

    public void setNextLevelKm(String nextLevelKm) {
        this.nextLevelKm = nextLevelKm;
    }
}
