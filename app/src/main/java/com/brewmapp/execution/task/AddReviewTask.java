package com.brewmapp.execution.task;

import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 04.11.2017.
 */

public class AddReviewTask extends BaseNetworkTask<ReviewPackage,String> {
    @Inject
    public AddReviewTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<String> prepareObservable(ReviewPackage reviewPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.REVIEW);
                params.addParam(Keys.RELATED_MODEL, reviewPackage.getRelated_model());
                params.addParam(Keys.RELATED_ID, reviewPackage.getRelated_id());
                params.addParam(Keys.TEXT, reviewPackage.getText());
                params.addParam(Keys.TYPE, reviewPackage.getType());
                Object o=executeCall(getApi().addReview(params));
                subscriber.onNext("");
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }
}
