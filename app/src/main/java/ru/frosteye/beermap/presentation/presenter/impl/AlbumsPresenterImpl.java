package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.entity.container.Albums;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.task.LoadAlbumsTask;
import ru.frosteye.beermap.execution.task.base.LoaderSubscriber;
import ru.frosteye.beermap.presentation.view.contract.AlbumsView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.AlbumsPresenter;

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
