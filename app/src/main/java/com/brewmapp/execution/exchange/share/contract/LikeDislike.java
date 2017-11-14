package com.brewmapp.execution.exchange.share.contract;

import android.content.Context;

import com.brewmapp.execution.exchange.response.base.MessageResponse;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by xpusher on 11/14/2017.
 */

public interface LikeDislike {
    void clickLike(String relatedModel, int relatedId,int type_like, SimpleSubscriber<MessageResponse> simpleSubscriber);
}
