package com.motorbike.anqi.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author lzx
 * @date 2017/12/4
 * @info
 */
@Entity
public class UsreBean {
    public String userName;
    @Id
    public String userId;
    public String heanImg;
    @Generated(hash = 448883681)
    public UsreBean(String userName, String userId, String heanImg) {
        this.userName = userName;
        this.userId = userId;
        this.heanImg = heanImg;
    }
    @Generated(hash = 1952331453)
    public UsreBean() {
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getHeanImg() {
        return this.heanImg;
    }
    public void setHeanImg(String heanImg) {
        this.heanImg = heanImg;
    }
}
