package com.brewmapp.presentation.presenter.impl;

import android.view.View;

import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.presentation.presenter.contract.ShareLikeViewPresenter;
import com.brewmapp.presentation.view.contract.RefreshableView;
import com.brewmapp.presentation.view.impl.widget.ShareLikeView;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 17.10.2017.
 */

public class ShareLikeViewPresenterImpl extends BasePresenter<ShareLikeView> implements ShareLikeViewPresenter {

    private LikeTask likeTask;

    @Inject
    public ShareLikeViewPresenterImpl(LikeTask likeTask){
        this.likeTask=likeTask;
    }


    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(ShareLikeView shareLikeView) {
        super.onAttach(shareLikeView);
    }

    @Override
    public void onLike(ILikeable iLikeable, RefreshableView refreshableView) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(LikeDislikePackage.TYPE_LIKE);
        if(iLikeable instanceof Event)
            likeDislikePackage.setModel(Keys.CAP_EVENT, ((Event)iLikeable).getId());
        else if (iLikeable instanceof Sale)
            likeDislikePackage.setModel(Keys.CAP_SHARE, ((Sale)iLikeable).getId());
        else if (iLikeable instanceof Post)
            likeDislikePackage.setModel(Keys.CAP_NEWS, ((Post)iLikeable).getId());

        likeTask.execute(likeDislikePackage, new LikeSubscriber(LikeDislikePackage.TYPE_LIKE, iLikeable, refreshableView));

    }

    @Override
    public void onDislike(ILikeable iLikeable, RefreshableView refreshableView) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(LikeDislikePackage.TYPE_DISLIKE);
        if(iLikeable instanceof Event)
            likeDislikePackage.setModel(Keys.CAP_EVENT, ((Event)iLikeable).getId());
        else if (iLikeable instanceof Sale)
            likeDislikePackage.setModel(Keys.CAP_SHARE, ((Sale)iLikeable).getId());
        else if (iLikeable instanceof Post)
            likeDislikePackage.setModel(Keys.CAP_NEWS, ((Post)iLikeable).getId());

        likeTask.execute(likeDislikePackage, new LikeSubscriber(LikeDislikePackage.TYPE_DISLIKE, iLikeable, refreshableView));
    }

    class LikeSubscriber extends SimpleSubscriber<MessageResponse> {

        private int  type = -1;
        private ILikeable iLikeable;
        private RefreshableView refreshableView;

        LikeSubscriber(int type, ILikeable iLikeable,RefreshableView refreshableView) {
            this.iLikeable = iLikeable;
            this.refreshableView = refreshableView;
            this.type = type;
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(MessageResponse messageResponse) {

            switch (type){
                case LikeDislikePackage.TYPE_DISLIKE:{
                    iLikeable.increaseDisLikes();
                    break;
                }
                case LikeDislikePackage.TYPE_LIKE:{
                    iLikeable.increaseLikes();
                    break;
                }
            }

            refreshableView.refreshState();
        }
    }

}
