package com.brewmapp.presentation.presenter.contract;

import java.io.File;

import com.brewmapp.data.entity.Photo;
import com.brewmapp.presentation.view.contract.AlbumView;

import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface AlbumPresenter extends LivePresenter<AlbumView> {
    void onRequestPhotos(int albumId, String user_id);
    void onUploadPhoto(int albumId, File file);
    void onLikePhoto(Photo photo, Callback<Boolean> callback);

    void deletePhoto(int photo_id, int albumId,SimpleSubscriber<Integer> simpleSubscriber);

    boolean isOwner(String user_id);
}
