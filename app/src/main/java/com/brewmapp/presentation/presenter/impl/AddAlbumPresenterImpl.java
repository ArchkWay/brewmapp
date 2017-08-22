package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.entity.Album;
import com.brewmapp.data.pojo.NewAlbumPackage;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.CreateAlbumTask;
import com.brewmapp.presentation.view.contract.AddAlbumView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.AddAlbumPresenter;

public class AddAlbumPresenterImpl extends BasePresenter<AddAlbumView> implements AddAlbumPresenter {

    private CreateAlbumTask createAlbumTask;

    @Inject
    public AddAlbumPresenterImpl(CreateAlbumTask createAlbumTask) {
        this.createAlbumTask = createAlbumTask;
    }

    @Override
    public void onDestroy() {
        createAlbumTask.cancel();
    }

    @Override
    public void onNewAlbumRequestReady(NewAlbumPackage newAlbumPackage) {
        enableControls(false);
        createAlbumTask.execute(newAlbumPackage, new SimpleSubscriber<SingleResponse<Album>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(SingleResponse<Album> albums) {
                enableControls(true);
                view.completeCreation();
            }
        });
    }
}
