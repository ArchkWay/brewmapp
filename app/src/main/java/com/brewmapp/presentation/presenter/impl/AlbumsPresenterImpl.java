package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.text.TextUtils;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Album;
import com.brewmapp.data.entity.container.Albums;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.DeleteAlbumTask;
import com.brewmapp.execution.task.LoadAlbumsTask;
import com.brewmapp.execution.task.base.LoaderSubscriber;
import com.brewmapp.presentation.view.contract.AlbumsView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.AlbumsPresenter;

public class AlbumsPresenterImpl extends BasePresenter<AlbumsView> implements AlbumsPresenter {

    private LoadAlbumsTask loadAlbumsTask;
    private DeleteAlbumTask deleteAlbumTask;
    private UserRepo userRepo;
    private String user_id;

    @Inject
    public AlbumsPresenterImpl(LoadAlbumsTask loadAlbumsTask, DeleteAlbumTask deleteAlbumTask,
                               UserRepo userRepo) {
        this.loadAlbumsTask = loadAlbumsTask;
        this.deleteAlbumTask = deleteAlbumTask;
        this.userRepo = userRepo;
    }

    @Override
    public void onDestroy() {
        loadAlbumsTask.cancel();
        deleteAlbumTask.cancel();
    }

    @Override
    public void onLoadAlbums(Intent intent) {
        enableControls(false);
        WrapperParams params = new WrapperParams(Wrappers.PHOTO_ALBUM);
        user_id=intent.getData().toString();
        if(!TextUtils.isEmpty(user_id)){
            params.addParam(Keys.USER_ID, user_id);
            loadAlbumsTask.execute(params, new LoaderSubscriber<Albums>(view) {
                @Override
                public void onResult(Albums result) {
                    view.showAlbums(result);
                }
            });

        }else {
            view.commonError();
        }
    }

    @Override
    public void onRemoveAlbum(Album album) {
        enableControls(false);
        deleteAlbumTask.execute(album.getId(), new SimpleSubscriber<MessageResponse>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                view.removeAlbumSuccess();
            }
        });
    }

    @Override
    public String getUserId() {
        return user_id;
    }

    @Override
    public boolean isOwnerUser() {
        return userRepo.load().getId()==Integer.valueOf(user_id);
    }
}
