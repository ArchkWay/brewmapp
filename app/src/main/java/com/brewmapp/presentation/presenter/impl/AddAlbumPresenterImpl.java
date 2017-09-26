package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.entity.Album;
import com.brewmapp.data.pojo.EditAlbumPackage;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.EditAlbumTask;
import com.brewmapp.presentation.view.contract.AddAlbumView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.AddAlbumPresenter;

public class AddAlbumPresenterImpl extends BasePresenter<AddAlbumView> implements AddAlbumPresenter {

    private EditAlbumTask editAlbumTask;

    @Inject
    public AddAlbumPresenterImpl(EditAlbumTask editAlbumTask) {
        this.editAlbumTask = editAlbumTask;
    }

    @Override
    public void onDestroy() {
        editAlbumTask.cancel();
    }

    @Override
    public void onNewAlbumRequestReady(EditAlbumPackage editAlbumPackage) {
        enableControls(false);
        editAlbumTask.execute(editAlbumPackage, new SimpleSubscriber<SingleResponse<Album>>() {
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
