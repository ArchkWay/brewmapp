package ru.frosteye.beermap.presentation.presenter.impl;

import java.io.File;

import javax.inject.Inject;

import ru.frosteye.beermap.data.entity.Post;
import ru.frosteye.beermap.data.entity.wrapper.PhotoPreviewInfo;
import ru.frosteye.beermap.data.pojo.NewPhotoPackage;
import ru.frosteye.beermap.execution.exchange.response.UploadPhotoResponse;
import ru.frosteye.beermap.execution.exchange.response.base.SingleResponse;
import ru.frosteye.beermap.execution.task.CreatePostTask;
import ru.frosteye.beermap.execution.task.UploadPhotoTask;
import ru.frosteye.beermap.presentation.view.contract.NewPostView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.NewPostPresenter;

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
