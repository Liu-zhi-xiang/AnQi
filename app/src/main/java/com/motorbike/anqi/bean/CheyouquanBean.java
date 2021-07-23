package com.motorbike.anqi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzx
 * @date 2018/4/25
 * @info
 */

public class CheyouquanBean implements Parcelable {



    /**
     * postId : 10
     * title : Woca
     * status : 1
     * content : 我和我的祖国一刻也不能分割
     * publishTime : 1524551713000
     * publishTimeStr : 23小时34分钟前
     * createBy : 1017
     * createTime : 2018-04-24 14:35:13.0
     * nickname : zhagnssang
     * name :
     * headerImg : http://172.21.59.2:8080/img/user/avatar/25898460-a0b4-4b17-a44c-0247f8533e2c.jpg
     * zan : 0
     * talk :
     * city : 上海
     * noteImgList : [{"id":200,"noteId":10,"imgUrl":"http://172.21.59.2:8080/img/app/noteImg/e8738e73-7f2b-49a7-bde4-710988fdc8b5.JPG"}]
     * noteBackImg : http://172.21.59.2:8080/img/app/noteImg/e8738e73-7f2b-49a7-bde4-710988fdc8b5.JPG
     * iszan : 0
     */


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
    /**
     * id : 200
     * noteId : 10
     * imgUrl : http://172.21.59.2:8080/img/app/noteImg/e8738e73-7f2b-49a7-bde4-710988fdc8b5.JPG
     */

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postId);
        dest.writeString(this.title);
        dest.writeString(this.status);
        dest.writeString(this.content);
        dest.writeLong(this.publishTime);
        dest.writeString(this.publishTimeStr);
        dest.writeString(this.createBy);
        dest.writeString(this.createTime);
        dest.writeString(this.nickname);
        dest.writeString(this.name);
        dest.writeString(this.headerImg);
        dest.writeString(this.zan);
        dest.writeString(this.talk);
        dest.writeString(this.city);
        dest.writeString(this.noteBackImg);
        dest.writeString(this.iszan);
        dest.writeList(this.noteImgList);
    }

    public CheyouquanBean() {
    }

    protected CheyouquanBean(Parcel in) {
        this.postId = in.readString();
        this.title = in.readString();
        this.status = in.readString();
        this.content = in.readString();
        this.publishTime = in.readLong();
        this.publishTimeStr = in.readString();
        this.createBy = in.readString();
        this.createTime = in.readString();
        this.nickname = in.readString();
        this.name = in.readString();
        this.headerImg = in.readString();
        this.zan = in.readString();
        this.talk = in.readString();
        this.city = in.readString();
        this.noteBackImg = in.readString();
        this.iszan = in.readString();
        this.noteImgList = new ArrayList<NoteImgListBean>();
        in.readList(this.noteImgList, NoteImgListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CheyouquanBean> CREATOR = new Parcelable.Creator<CheyouquanBean>() {
        @Override
        public CheyouquanBean createFromParcel(Parcel source) {
            return new CheyouquanBean(source);
        }

        @Override
        public CheyouquanBean[] newArray(int size) {
            return new CheyouquanBean[size];
        }
    };
}
