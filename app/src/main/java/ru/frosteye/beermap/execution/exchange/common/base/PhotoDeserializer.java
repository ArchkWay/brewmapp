package ru.frosteye.beermap.execution.exchange.common.base;

import ru.frosteye.beermap.data.entity.Album;
import ru.frosteye.beermap.data.entity.Photo;
import ru.frosteye.beermap.data.entity.wrapper.AlbumInfo;
import ru.frosteye.beermap.data.entity.wrapper.PhotoInfo;
import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by oleg on 16.08.17.
 */

public class PhotoDeserializer extends AdapterItemDeserializer<Photo, PhotoInfo> {
    @Override
    protected Class<Photo> provideType() {
        return Photo.class;
    }

    @Override
    protected Class<PhotoInfo> provideWrapperType() {
        return PhotoInfo.class;
    }
}
