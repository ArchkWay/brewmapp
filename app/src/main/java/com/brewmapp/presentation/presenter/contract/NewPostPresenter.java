package com.brewmapp.presentation.presenter.contract;

import java.io.File;

import com.brewmapp.data.entity.Post;
import com.brewmapp.presentation.view.contract.NewPostView;
import com.brewmapp.presentation.view.contract.ResultTask;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface NewPostPresenter extends LivePresenter<NewPostView> {
    void onUploadPhotoRequest(File file);
    void onPostReady(Post post, ResultTask resultTask);
}
