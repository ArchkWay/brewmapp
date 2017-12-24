package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

import com.brewmapp.data.model.IPerson;
import com.brewmapp.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 17.08.17.
 */

public class Contact implements IPerson {

    @SerializedName(Keys.FRIEND_ID)
    private int id;

    private int status;

    @SerializedName(Keys.USER_INFO)
    private User user;

    private User friend_info;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend_info() {
        return friend_info;
    }

    public void setFriend_info(User friend_info) {
        this.friend_info = friend_info;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getTitle() {
        return getUser().getFormattedName();
    }

    @Override
    public String getImageUrl() {
        return getUser().getThumbnail();
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
