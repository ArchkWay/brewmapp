package com.brewmapp.presentation.presenter.impl;

import android.text.TextUtils;

import java.io.File;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.container.AlbumPhotos;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.NewPhotoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.DeletePhotoTask;
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
    private DeletePhotoTask deletePhotoTask;
    private UserRepo userRepo;
    @Inject
    public AlbumPresenterImpl(UploadPhotoTask uploadPhotoTask,
                              LoadAlbumPhotosTask loadAlbumPhotosTask,
                              LikeTask likeTask,
                              UserRepo userRepo,
                              DeletePhotoTask deletePhotoTask) {
        this.uploadPhotoTask = uploadPhotoTask;
        this.loadAlbumPhotosTask = loadAlbumPhotosTask;
        this.likeTask = likeTask;
        this.deletePhotoTask = deletePhotoTask;
        this.userRepo = userRepo;
    }

    @Override
    public void onDestroy() {
        uploadPhotoTask.cancel();
        loadAlbumPhotosTask.cancel();
    }

    @Override
    public void onRequestPhotos(int albumId, String user_id) {
        if(!TextUtils.isEmpty(user_id)) {
            enableControls(false);
            WrapperParams wrapperParams = new WrapperParams(Wrappers.PHOTO);
            if (albumId == 0) {
                wrapperParams.addParam(Keys.RELATED_MODEL, Keys.CAP_USER);
                wrapperParams.addParam(Keys.RELATED_ID, user_id);
            } else {
                wrapperParams.addParam(Keys.PHOTO_ALBUM_ID, albumId);
            }
            loadAlbumPhotosTask.execute(wrapperParams, new SimpleSubscriber<AlbumPhotos>() {
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
        }else {
            view.commonError();
        }
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
                view.uploadPhotoSuccess();
                file.delete();
            }
        });
    }

    @Override
    public void onLikePhoto(Photo photo, Callback<Boolean> callback) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(Keys.TYPE_LIKE);
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

    @Override
    public void deletePhoto(int photo_id, final int albumId, SimpleSubscriber<Integer> simpleSubscriber) {
        WrapperParams wrapperParams=new WrapperParams(Keys.CAP_PHOTO);
        wrapperParams.addParam(Keys.ID,photo_id);
        deletePhotoTask.execute(wrapperParams,simpleSubscriber);
    }

    @Override
    public boolean isOwner(String user_id) {
        return userRepo.load().getId()==Integer.valueOf(user_id);
    }

    private class LikeSubscriber extends SimpleSubscriber<MessageResponse> {

        private ILikeable iLikeable;

        private LikeSubscriber(ILikeable iLikeable) {
            this.iLikeable = iLikeable;
        }


    }
}
