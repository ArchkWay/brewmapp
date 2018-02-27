package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;

import com.brewmapp.data.entity.Photo;
import com.brewmapp.presentation.view.impl.widget.PhotoView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 16.08.17.
 */

public class PhotoInfo extends AdapterItem<Photo, PhotoView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_photo;
    }

}
