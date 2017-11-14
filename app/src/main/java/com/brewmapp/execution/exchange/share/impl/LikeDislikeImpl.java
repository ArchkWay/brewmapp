package com.brewmapp.execution.exchange.share.impl;

import android.content.Context;

import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.share.contract.LikeDislike;
import com.brewmapp.execution.task.LikeTask;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by xpusher on 11/14/2017.
 */

public class LikeDislikeImpl implements LikeDislike{
    private LikeTask likeTask;
    @Inject
    public LikeDislikeImpl(LikeTask likeTask){
        this.likeTask=likeTask;
    }

    @Override
    public void clickLike(String relatedModel, int relatedId, int type_like, SimpleSubscriber<MessageResponse> simpleSubscriber) {

        LikeDislikePackage likeDislikePackage=new LikeDislikePackage(type_like);
        likeDislikePackage.setModel(relatedModel,relatedId);
        likeTask.execute(likeDislikePackage,new SimpleSubscriber<MessageResponse>(){
            @Override
            public void onError(Throwable e) {
                super.onError(e);if (simpleSubscriber!=null)simpleSubscriber.onError(e);
            }
            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);if (simpleSubscriber!=null)simpleSubscriber.onNext(messageResponse);
            }
        });

    }
}
