package ru.frosteye.beermap.presentation.presenter.contract;

import ru.frosteye.beermap.presentation.view.contract.AlbumsView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface AlbumsPresenter extends LivePresenter<AlbumsView> {
    void onLoadAlbums();
}
