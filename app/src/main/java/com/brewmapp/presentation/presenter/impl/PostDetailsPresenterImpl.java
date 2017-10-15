package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.presenter.contract.PostDetailsPresenter;
import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;
import com.brewmapp.presentation.view.contract.PostDetailsView;
import com.brewmapp.presentation.view.contract.SaleDetailsView;

import javax.inject.Inject;

import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

public class PostDetailsPresenterImpl extends BasePresenter<PostDetailsView> implements PostDetailsPresenter {

    private ActiveBox activeBox;
    private LikeTask likeTask;

    @Inject
    public PostDetailsPresenterImpl(ActiveBox activeBox, LikeTask likeTask) {
        this.activeBox=activeBox;
        this.likeTask=likeTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(PostDetailsView postDetailsView) {
        super.onAttach(postDetailsView);
        view.showPostDetails(activeBox.getActive(Post.class));
    }

}
