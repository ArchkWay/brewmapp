package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.container.Albums;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.LoadAlbumsTask;
import com.brewmapp.execution.task.base.LoaderSubscriber;
import com.brewmapp.presentation.view.contract.AlbumsView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.AlbumsPresenter;

public class AlbumsPresenterImpl extends BasePresenter<AlbumsView> implements AlbumsPresenter {

    private LoadAlbumsTask loadAlbumsTask;
    private UserRepo userRepo;

    @Inject
    public AlbumsPresenterImpl(LoadAlbumsTask loadAlbumsTask, UserRepo userRepo) {
        this.loadAlbumsTask = loadAlbumsTask;
        this.userRepo = userRepo;
    }

    @Override
    public void onDestroy() {
        loadAlbumsTask.cancel();
    }

    @Override
    public void onLoadAlbums() {
        enableControls(false);
        WrapperParams params = new WrapperParams(Wrappers.PHOTO_ALBUM);
        params.addParam(Keys.USER_ID, userRepo.load().getId());
        loadAlbumsTask.execute(params, new LoaderSubscriber<Albums>(view) {
            @Override
            public void onResult(Albums result) {
                view.showAlbums(result);
            }
        });
    }
}
