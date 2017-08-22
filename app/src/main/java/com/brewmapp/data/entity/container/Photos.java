package com.brewmapp.data.entity.container;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.brewmapp.data.entity.Photo;
import com.brewmapp.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 16.08.17.
 */

public class Photos {
    @SerializedName(Keys.PHOTOS)
    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }
}
