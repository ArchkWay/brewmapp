package com.brewmapp.presentation.presenter.contract;

import java.io.File;

import com.brewmapp.presentation.view.contract.AlbumView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface AlbumPresenter extends LivePresenter<AlbumView> {
    void onRequestPhotos(int albumId);
    void onUploadPhoto(int albumId, File file);
}
