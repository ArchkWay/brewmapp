package ru.frosteye.beermap.presentation.presenter.contract;

import java.io.File;

import ru.frosteye.beermap.data.entity.Post;
import ru.frosteye.beermap.presentation.view.contract.NewPostView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface NewPostPresenter extends LivePresenter<NewPostView> {
    void onUploadPhotoRequest(File file);
    void onPostReady(Post post);
}
