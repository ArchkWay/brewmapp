package com.brewmapp.presentation.presenter.impl;

import java.io.File;

import javax.inject.Inject;

import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.wrapper.PhotoPreviewInfo;
import com.brewmapp.data.pojo.NewPhotoPackage;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.CreatePostTask;
import com.brewmapp.execution.task.UploadPhotoTask;
import com.brewmapp.presentation.view.contract.NewPostView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.NewPostPresenter;

public class NewPostPresenterImpl extends BasePresenter<NewPostView> implements NewPostPresenter {

    private UploadPhotoTask uploadPhotoTask;
    private CreatePostTask createPostTask;

    @Inject
    public NewPostPresenterImpl(UploadPhotoTask uploadPhotoTask, CreatePostTask createPostTask) {
        this.uploadPhotoTask = uploadPhotoTask;
        this.createPostTask = createPostTask;
    }

    @Override
    public void onDestroy() {
        uploadPhotoTask.cancel();
        createPostTask.cancel();
    }

    @Override
    public void onUploadPhotoRequest(File file) {
        enableControls(false);
        NewPhotoPackage newPhotoPackage = new NewPhotoPackage(file);
        uploadPhotoTask.execute(newPhotoPackage, new SimpleSubscriber<SingleResponse<UploadPhotoResponse>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(SingleResponse<UploadPhotoResponse> uploadPhotoResponseSingleResponse) {
                enableControls(true);
                UploadPhotoResponse response = uploadPhotoResponseSingleResponse.getData();
                response.setFile(file);
                view.addPhoto(new PhotoPreviewInfo(response));
            }
        });
    }

    @Override
    public void onPostReady(Post post) {
        enableControls(false);
        createPostTask.execute(post, new SimpleSubscriber<SingleResponse<Post>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(SingleResponse<Post> postSingleResponse) {
                enableControls(true);
                view.complete();
            }
        });
    }
}
