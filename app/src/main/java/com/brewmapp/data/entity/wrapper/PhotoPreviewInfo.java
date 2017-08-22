package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.presentation.view.impl.widget.PhotoPreviewView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 17.08.17.
 */

public class PhotoPreviewInfo extends AdapterItem<UploadPhotoResponse, PhotoPreviewView> {

    public PhotoPreviewInfo(UploadPhotoResponse model) {
        super(model);
    }

    public PhotoPreviewInfo() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_photo_preview;
    }
}
