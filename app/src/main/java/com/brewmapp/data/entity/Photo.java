package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.data.model.ILikeable;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import com.brewmapp.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 16.08.17.
 */

public class Photo implements ILikeable,Serializable{
    private int id;
    private int like;
    private boolean selected;

    private String title;

    @SerializedName(Keys.DIS_LIKE)
    private int dislike;

    @SerializedName(Keys.FRONT_PHOTO)
    private String frontPhoto;

    @SerializedName(Keys.TIMESTAMP)
    private Date createdAt;

    @SerializedName(Keys.USER_INFO)
    private User user;

    private String url;

    private Info info;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @SerializedName(Keys.GET_THUMB)
    private Thumb thumb;

    public Thumb getThumb() {
        return thumb;
    }

    @SerializedName(Keys.URL_PREVIEW)
    private String urlPreview;

    private Size size;

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public Size getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public int getLike() {
        return like;
    }

    public String getTitle() {
        return title;
    }

    public int getDislike() {
        return dislike;
    }

    public String getFrontPhoto() {
        return frontPhoto;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUrl() {
        if(url != null && !url.startsWith("http")) {
            url = BuildConfig.SERVER_ROOT_URL + url;
        }
        return url;
    }

    public String getUrlPreview() {
        if(urlPreview != null && !urlPreview.startsWith("http")&& !url.startsWith("/")) {
            urlPreview = BuildConfig.SERVER_ROOT_URL + urlPreview;
        }
        return urlPreview;
    }

    @Override
    public void increaseLikes() {
        like++;
    }

    @Override
    public void increaseDisLikes() {
        dislike++;
    }


    public static class Size implements Serializable {
        @SerializedName("0")
        private int width;
        @SerializedName("1")
        private int height;

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
