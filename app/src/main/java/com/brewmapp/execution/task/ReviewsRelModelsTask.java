package com.brewmapp.execution.task;

import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.data.entity.LocalizedStrings;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 30.11.2017.
 */

public class ReviewsRelModelsTask extends BaseNetworkTask<String, SingleResponse<LocalizedStrings>> {

    @Inject
    public ReviewsRelModelsTask(MainThread mainThread,
                                Executor executor,
                                Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<SingleResponse<LocalizedStrings>> prepareObservable(String val) {
        return Observable.create(subscriber -> {
            try {
                SingleResponse<LocalizedStrings> response = Paper.book().read(FilterKeys.REVIEWS_RELATED_MODELS);
                if(response == null){
                    response = executeCall(getApi().getReviewsRelModels());
                    Paper.book().write(FilterKeys.REVIEWS_RELATED_MODELS,response);
                }

                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}

