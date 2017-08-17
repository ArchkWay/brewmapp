package ru.frosteye.beermap.data.entity;

import com.google.gson.annotations.SerializedName;

import ru.frosteye.beermap.data.model.IPerson;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;

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
