package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.wrapper.PhotoInfo;
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
