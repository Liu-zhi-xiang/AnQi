package com.motorbike.anqi.bean;

/**
 * @author lzx
 * @date 2017/11/13
 * @info
 */

public class ForumBean {

    public String id;
    public String getIsend() {
        return isend;
    }

    public void setIsend(String isend) {
        this.isend = isend;
    }

    public  int type;

    public int imgmub=0;
    public String isend;
    public String talk;
    public int fuNub;
    public TalkListBean getLuntanBean() {
        return luntanBean;
    }

    public void setLuntanBean(TalkListBean luntanBean) {
        this.luntanBean = luntanBean;
    }

    public ReplyListBean getTalksBean() {
        return talksBean;
    }

    public void setTalksBean(ReplyListBean talksBean) {
        this.talksBean = talksBean;
    }

    public TalkListBean luntanBean;
    public ReplyListBean talksBean;
    public CheyouquanBean cheyouquanBean;

    public CheyouquanBean getCheyouquanBean() {
        return cheyouquanBean;
    }

    public void setCheyouquanBean(CheyouquanBean cheyouquanBean) {
        this.cheyouquanBean = cheyouquanBean;
    }

    public ForumBean(int type,CheyouquanBean cheyouquanBean,TalkListBean luntanBean, ReplyListBean talksBean, String talk,int fuNub) {
        this.type = type;
        this.luntanBean = luntanBean;
        this.talksBean = talksBean;
        this.talk = talk;
        this.cheyouquanBean=cheyouquanBean;
        this.fuNub=fuNub;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public int getImgmub() {
        return imgmub;
    }

    public void setImgmub(int imgmub) {
        this.imgmub = imgmub;
    }


    public ForumBean(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
