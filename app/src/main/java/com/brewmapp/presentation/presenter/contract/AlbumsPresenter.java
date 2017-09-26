package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.Album;
import com.brewmapp.presentation.view.contract.AlbumsView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface AlbumsPresenter extends LivePresenter<AlbumsView> {
    void onLoadAlbums();
    void onRemoveAlbum(Album album);
}
