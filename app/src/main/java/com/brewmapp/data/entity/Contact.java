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
}
