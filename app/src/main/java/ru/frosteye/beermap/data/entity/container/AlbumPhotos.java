package ru.frosteye.beermap.data.entity.container;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.frosteye.beermap.data.entity.wrapper.PhotoInfo;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.response.base.ListResponse;

/**
 * Created by oleg on 16.08.17.
 */

public class AlbumPhotos extends ListResponse<PhotoInfo> {
    public AlbumPhotos(
            @NonNull List<PhotoInfo> models) {
        super(models);
    }
}
