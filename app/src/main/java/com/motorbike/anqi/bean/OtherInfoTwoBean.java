package com.motorbike.anqi.bean;

/**
 * Created by Administrator on 2018/4/26.
 */

public class OtherInfoTwoBean {
    private int type;
    private NoteListBean noteListBean;
    private TripListBean tripListBean;
    private OtherDetailBean otherDetailBean;

    public OtherInfoTwoBean(int type, NoteListBean noteListBean, TripListBean tripListBean, OtherDetailBean otherDetailBean) {
        this.type = type;
        this.noteListBean = noteListBean;
        this.tripListBean = tripListBean;
        this.otherDetailBean = otherDetailBean;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NoteListBean getNoteListBean() {
        return noteListBean;
    }

    public void setNoteListBean(NoteListBean noteListBean) {
        this.noteListBean = noteListBean;
    }

    public TripListBean getTripListBean() {
        return tripListBean;
    }

    public void setTripListBean(TripListBean tripListBean) {
        this.tripListBean = tripListBean;
    }

    public OtherDetailBean getOtherDetailBean() {
        return otherDetailBean;
    }

    public void setOtherDetailBean(OtherDetailBean otherDetailBean) {
        this.otherDetailBean = otherDetailBean;
    }
}
