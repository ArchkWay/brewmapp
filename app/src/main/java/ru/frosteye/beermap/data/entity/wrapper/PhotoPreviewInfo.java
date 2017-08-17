package ru.frosteye.beermap.data.entity.wrapper;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.execution.exchange.response.UploadPhotoResponse;
import ru.frosteye.beermap.presentation.view.impl.widget.PhotoPreviewView;
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
