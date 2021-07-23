package com.motorbike.anqi.bean;

/**
 * @author lzx
 * @date 2018/4/25
 * @info
 */

public class ReplyListBean {

    public ReplyListBean(String headerImg, String nickname, String content,String publishTimeStr, String id, String fromUid, String toUid, String toreplyName)
    {
        this.headerImg = headerImg;
        this.nickname = nickname;
        this.content = content;
        this.publishTimeStr = publishTimeStr;
        this.id = id;
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.toreplyName = toreplyName;
    }

    /**
     * headerImg : http://172.21.59.2:8080/img/user/avatar/2dc23cb0-41a9-42b9-aea5-7a7945539083.png
     * nickname : 王五
     * content : 市规划局即可市规划局即可市规划局即可
     * publishTime : 1524116207000
     * publishTimeStr : 6天前13点36分
     * id : 6
     * fromUid : 1001
     * toUid : 1007
     * toreplyName : 赵六
     */

    private String headerImg;
    private String nickname;
    private String content;
    private long publishTime;
    private String publishTimeStr;
    private String id;
    private String fromUid;
    private String toUid;
    private String toreplyName;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getToreplyName() {
        return toreplyName;
    }

    public void setToreplyName(String toreplyName) {
        this.toreplyName = toreplyName;
    }
}
