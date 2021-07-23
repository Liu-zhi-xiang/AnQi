package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/20.
 */

public class MsgListBean {

    /**
     * id : 31
     * messageName : 111113457899
     * messageContent : 244555555555
     * messageType : 1
     * userAccount : null
     * messageTime : 2018-04-05 19:45:00.0
     * isRead : 0
     */

    private String id;
    private String messageName;
    private String messageContent;
    private String messageType;
    private Object userAccount;
    private String messageTime;
    private String isRead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Object getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(Object userAccount) {
        this.userAccount = userAccount;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
