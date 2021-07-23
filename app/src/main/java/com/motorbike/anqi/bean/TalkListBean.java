package com.motorbike.anqi.bean;

import java.util.List;

/**
 * @author lzx
 * @date 2018/4/25
 * @info
 */

public class TalkListBean {

    public TalkListBean(String talkId) {
        this.talkId = talkId;
    }
    public TalkListBean() {

    }

    public TalkListBean(String headerImg, String nickname, String talkContent, long publishTime, String publishTimeStr, String talkId, String fromUid, String toUid, List<ReplyListBean> replyList) {
        this.headerImg = headerImg;
        this.nickname = nickname;
        this.talkContent = talkContent;
        this.publishTime = publishTime;
        this.publishTimeStr = publishTimeStr;
        this.talkId = talkId;
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.replyList = replyList;
    }

    private String headerImg;
    private String nickname;
    private String talkContent;
    private long publishTime;
    private String publishTimeStr;
    private String talkId;
    private String fromUid;
    private String toUid;
    private List<ReplyListBean> replyList;

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTalkContent() {
        return talkContent;
    }

    public void setTalkContent(String talkContent) {
        this.talkContent = talkContent;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishTimeStr() {
        return publishTimeStr;
    }

    public void setPublishTimeStr(String publishTimeStr) {
        this.publishTimeStr = publishTimeStr;
    }

    public String getTalkId() {
        return talkId;
    }

    public void setTalkId(String talkId) {
        this.talkId = talkId;
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public List<ReplyListBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ReplyListBean> replyList) {
        this.replyList = replyList;
    }
}
