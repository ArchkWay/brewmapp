package ru.frosteye.beermap.presentation.presenter.contract;

import ru.frosteye.beermap.data.pojo.NewAlbumPackage;
import ru.frosteye.beermap.presentation.view.contract.AddAlbumView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface AddAlbumPresenter extends LivePresenter<AddAlbumView> {
    void onNewAlbumRequestReady(NewAlbumPackage newAlbumPackage);
}
