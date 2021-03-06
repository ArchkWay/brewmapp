package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import com.brewmapp.data.entity.container.Photos;
import com.brewmapp.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 16.08.17.
 */

public class Album implements Serializable {
    private int id;
    private String description;
    private String name;
    private int like;
    private int interested;


    public Album() {
    }

    public Album(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @SerializedName(Keys.DIS_LIKE)
    private int dislike;

    @SerializedName(Keys.NO_INTERESTED)
    private int notInterested;

    @SerializedName(Keys.CREATED_AT)
    private Date createdAt;

    @SerializedName(Keys.USER_INFO)
    private User user;

    @SerializedName(Keys.RELATIONS)
    private Photos photos;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getLike() {
        return like;
    }

    public int getInterested() {
        return interested;
    }

    public int getDislike() {
        return dislike;
    }

    public int getNotInterested() {
        return notInterested;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public Photos getPhotos() {
        return photos;
    }

    public static class SimpleAlbum implements Serializable {

        private int albumId;
        private String title;
        private String description;

        public SimpleAlbum(Album album) {
            this.albumId = album.getId();
            this.title = album.getName();
            this.description = album.getDescription();
        }

        public int getAlbumId() {
            return albumId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
