package com.motorbike.anqi.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class NoteListBean {
    private String postId;
    private String title;
    private String status;
    private String content;
    private long publishTime;
    private String publishTimeStr;
    private String createBy;
    private String createTime;
    private String nickname;
    private String name;
    private String headerImg;
    private String zan;
    private String talk;
    private String city;
    private String noteBackImg;
    private String iszan;
    private List<NoteImgListBean> noteImgList;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNoteBackImg() {
        return noteBackImg;
    }

    public void setNoteBackImg(String noteBackImg) {
        this.noteBackImg = noteBackImg;
    }

    public String getIszan() {
        return iszan;
    }

    public void setIszan(String iszan) {
        this.iszan = iszan;
    }

    public List<NoteImgListBean> getNoteImgList() {
        return noteImgList;
    }

    public void setNoteImgList(List<NoteImgListBean> noteImgList) {
        this.noteImgList = noteImgList;
    }
}
