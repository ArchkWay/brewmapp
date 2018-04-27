package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.container.AlbumPhotos;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface AlbumView extends BasicView {
    void showPhotos(AlbumPhotos photos);

    void commonError(String... strings);

    void uploadPhotoSuccess();
}
