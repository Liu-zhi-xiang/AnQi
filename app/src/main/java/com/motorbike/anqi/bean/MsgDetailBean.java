package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/20.
 */

public class MsgDetailBean {

    /**
     * id : 31
     * messageName : 111113457899
     * messageContent : <p>244555555555</p>

     * messageType : 1
     * userAccount : null
     * messageTime : 1522928700000
     * sendTime : 2018-04-05 07:45:00
     */

    private int id;
    private String messageName;
    private String messageContent;
    private int messageType;
    private Object userAccount;
    private long messageTime;
    private String sendTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(Object userAccount) {
        this.userAccount = userAccount;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
