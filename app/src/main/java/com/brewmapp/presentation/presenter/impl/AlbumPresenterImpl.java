package com.brewmapp.presentation.presenter.impl;

import java.io.File;

import javax.inject.Inject;

import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.container.AlbumPhotos;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.NewPhotoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.execution.task.LoadAlbumPhotosTask;
import com.brewmapp.execution.task.UploadPhotoTask;
import com.brewmapp.presentation.view.contract.AlbumView;

import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.AlbumPresenter;

public class AlbumPresenterImpl extends BasePresenter<AlbumView> implements AlbumPresenter {

    private UploadPhotoTask uploadPhotoTask;
    private LoadAlbumPhotosTask loadAlbumPhotosTask;
    private LikeTask likeTask;

    @Inject
    public AlbumPresenterImpl(UploadPhotoTask uploadPhotoTask,
                              LoadAlbumPhotosTask loadAlbumPhotosTask, LikeTask likeTask) {
        this.uploadPhotoTask = uploadPhotoTask;
        this.loadAlbumPhotosTask = loadAlbumPhotosTask;
        this.likeTask = likeTask;
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

    @Override
    public void onLikePhoto(Photo photo, Callback<Boolean> callback) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(LikeDislikePackage.TYPE_LIKE);
        likeDislikePackage.setModel(Keys.CAP_PHOTO, photo.getId());
        likeTask.execute(likeDislikePackage, new SimpleSubscriber<MessageResponse>() {
            @Override
            public void onError(Throwable e) {
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                photo.increaseLikes();
                callback.onResult(true);
            }
        });
    }

    private class LikeSubscriber extends SimpleSubscriber<MessageResponse> {

        private ILikeable iLikeable;

        private LikeSubscriber(ILikeable iLikeable) {
            this.iLikeable = iLikeable;
        }


    }
}
