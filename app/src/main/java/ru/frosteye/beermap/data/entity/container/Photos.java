package ru.frosteye.beermap.data.entity.container;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.frosteye.beermap.data.entity.Photo;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;

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
