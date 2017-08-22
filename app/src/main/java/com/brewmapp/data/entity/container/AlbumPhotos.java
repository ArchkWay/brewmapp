package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import java.util.List;

import com.brewmapp.data.entity.wrapper.PhotoInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

/**
 * Created by oleg on 16.08.17.
 */

public class AlbumPhotos extends ListResponse<PhotoInfo> {
    public AlbumPhotos(
            @NonNull List<PhotoInfo> models) {
        super(models);
    }
}
