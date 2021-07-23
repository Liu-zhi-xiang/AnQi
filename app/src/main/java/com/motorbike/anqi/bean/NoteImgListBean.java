package com.motorbike.anqi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lzx
 * @date 2018/4/25
 * @info
 */

public class NoteImgListBean implements Parcelable {
    private int id;
    private int noteId;
    private String imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.noteId);
        dest.writeString(this.imgUrl);
    }

    public NoteImgListBean() {
    }

    protected NoteImgListBean(Parcel in) {
        this.id = in.readInt();
        this.noteId = in.readInt();
        this.imgUrl = in.readString();
    }

    public static final Parcelable.Creator<NoteImgListBean> CREATOR = new Parcelable.Creator<NoteImgListBean>() {
        @Override
        public NoteImgListBean createFromParcel(Parcel source) {
            return new NoteImgListBean(source);
        }

        @Override
        public NoteImgListBean[] newArray(int size) {
            return new NoteImgListBean[size];
        }
    };
}
