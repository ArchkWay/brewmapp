package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.EditAlbumPackage;
import com.brewmapp.presentation.view.contract.AddAlbumView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface AddAlbumPresenter extends LivePresenter<AddAlbumView> {
    void onNewAlbumRequestReady(EditAlbumPackage editAlbumPackage);
}
