package ru.frosteye.beermap.presentation.presenter.contract;

import java.io.File;

import ru.frosteye.beermap.presentation.view.contract.AlbumView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface AlbumPresenter extends LivePresenter<AlbumView> {
    void onRequestPhotos(int albumId);
    void onUploadPhoto(int albumId, File file);
}
