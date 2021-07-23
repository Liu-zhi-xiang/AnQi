package com.motorbike.anqi.bean;

/**
 * @author lzx
 * @date 2018/1/22
 * @info
 */

public class Voiceroom {
    private String roomId;
    private String roomname;
    private String roomTitle;
    private boolean xiala=false;

    public Voiceroom(String roomId, String roomname, String roomTitle, boolean xiala) {
        this.roomId = roomId;
        this.roomname = roomname;
        this.roomTitle = roomTitle;
        this.xiala = xiala;
    }

    public Voiceroom(String roomId, String roomname, String roomTitle) {
        this.roomId = roomId;
        this.roomname = roomname;
        this.roomTitle = roomTitle;
    }

    public Voiceroom(boolean xiala) {
        this.xiala = xiala;
    }

    public boolean isXiala() {

        return xiala;
    }

    public void setXiala(boolean xiala) {
        this.xiala = xiala;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }
}
