package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import com.brewmapp.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 16.08.17.
 */

public class Photo {
    private int id;
    private int like;

    private String title;

    @SerializedName(Keys.DIS_LIKE)
    private int dislike;

    @SerializedName(Keys.FRONT_PHOTO)
    private String frontPhoto;

    @SerializedName(Keys.TIMESTAMP)
    private Date createdAt;

    private String url;

    @SerializedName(Keys.GET_THUMB)
    private Thumb thumb;

    public Thumb getThumb() {
        return thumb;
    }

    @SerializedName(Keys.URL_PREVIEW)
    private String urlPreview;

    private Size size;

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
        return url;
    }

    public String getUrlPreview() {
        return urlPreview;
    }

    public static class Thumb {
        private String url;

        @SerializedName(Keys.URL_PREVIEW)
        private String thumbUrl;

        public String getUrl() {
            return url;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }
    }

    public static class Size {
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
