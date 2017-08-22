package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Album;
import com.brewmapp.data.entity.wrapper.AlbumInfo;
import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by oleg on 16.08.17.
 */

public class AlbumsDeserializer extends AdapterItemDeserializer<Album, AlbumInfo> {
    @Override
    protected Class<Album> provideType() {
        return Album.class;
    }

    @Override
    protected Class<AlbumInfo> provideWrapperType() {
        return AlbumInfo.class;
    }
}
