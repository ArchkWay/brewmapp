package ru.frosteye.beermap.data.entity.container;

import android.support.annotation.NonNull;

import java.util.List;

import ru.frosteye.beermap.data.entity.wrapper.AlbumInfo;
import ru.frosteye.beermap.execution.exchange.response.base.ListResponse;

/**
 * Created by oleg on 16.08.17.
 */

public class Albums extends ListResponse<AlbumInfo> {
    public Albums(@NonNull List<AlbumInfo> models) {
        super(models);
    }
}
