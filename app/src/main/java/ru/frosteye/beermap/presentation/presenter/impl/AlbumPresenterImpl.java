package ru.frosteye.beermap.presentation.presenter.impl;

import java.io.File;

import javax.inject.Inject;

import ru.frosteye.beermap.data.entity.container.AlbumPhotos;
import ru.frosteye.beermap.data.pojo.NewPhotoPackage;
import ru.frosteye.beermap.execution.exchange.response.UploadPhotoResponse;
import ru.frosteye.beermap.execution.exchange.response.base.SingleResponse;
import ru.frosteye.beermap.execution.task.LoadAlbumPhotosTask;
import ru.frosteye.beermap.execution.task.UploadPhotoTask;
import ru.frosteye.beermap.presentation.view.contract.AlbumView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.AlbumPresenter;

public class AlbumPresenterImpl extends BasePresenter<AlbumView> implements AlbumPresenter {

    private UploadPhotoTask uploadPhotoTask;
    private LoadAlbumPhotosTask loadAlbumPhotosTask;

    @Inject
    public AlbumPresenterImpl(UploadPhotoTask uploadPhotoTask,
                              LoadAlbumPhotosTask loadAlbumPhotosTask) {
        this.uploadPhotoTask = uploadPhotoTask;
        this.loadAlbumPhotosTask = loadAlbumPhotosTask;
    }

    @Override
    public void onDestroy() {
        uploadPhotoTask.cancel();
        loadAlbumPhotosTask.cancel();
    }

    @Override
    public void onRequestPhotos(int albumId) {
        enableControls(false);
        loadAlbumPhotosTask.execute(albumId, new SimpleSubscriber<AlbumPhotos>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(AlbumPhotos albumPhotos) {
                enableControls(true);
                view.showPhotos(albumPhotos);
            }
        });
    }

    @Override
    public void onUploadPhoto(int albumId, File file) {
        enableControls(false);
        uploadPhotoTask.execute(new NewPhotoPackage(file, albumId), new SimpleSubscriber<SingleResponse<UploadPhotoResponse>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(SingleResponse<UploadPhotoResponse> responseBody) {
                enableControls(true);
                onRequestPhotos(albumId);
            }
        });
    }
}
