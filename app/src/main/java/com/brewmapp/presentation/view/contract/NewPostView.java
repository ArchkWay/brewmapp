package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.wrapper.PhotoPreviewInfo;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface NewPostView extends BasicView {
    void addPhoto(PhotoPreviewInfo photo);
    void complete();
}
