package ru.frosteye.beermap.presentation.view.contract;

import ru.frosteye.beermap.data.entity.wrapper.PhotoPreviewInfo;
import ru.frosteye.beermap.execution.exchange.response.UploadPhotoResponse;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface NewPostView extends BasicView {
    void addPhoto(PhotoPreviewInfo photo);
    void complete();
}
