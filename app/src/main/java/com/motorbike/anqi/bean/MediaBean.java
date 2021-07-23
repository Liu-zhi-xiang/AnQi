package com.motorbike.anqi.bean;

import android.net.Uri;

/**
 * @author lzx
 * @date 2018/1/24
 * @info
 */

public class MediaBean {
    private String id;
    private int width;
    private int height;
    private Uri uri;

    public MediaBean(String id, Uri uri) {
        this.id = id;
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setOriginalPath(String originalPath) {
        OriginalPath = originalPath;
    }

    public String getThumbnailBigPath() {
        return ThumbnailBigPath;
    }

    public void setThumbnailBigPath(String thumbnailBigPath) {
        ThumbnailBigPath = thumbnailBigPath;
    }

    private String OriginalPath;
    private String ThumbnailBigPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getOriginalPath() {
        return OriginalPath;
    }
}
