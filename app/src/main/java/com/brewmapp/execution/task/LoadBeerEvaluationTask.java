package com.brewmapp.execution.task;

import com.brewmapp.data.entity.EvaluationBeer;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 21.01.2018.
 */

public class LoadBeerEvaluationTask  extends BaseNetworkTask<Integer, ArrayList<EvaluationBeer>> {

    @Inject
    public LoadBeerEvaluationTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<ArrayList<EvaluationBeer>> prepareObservable(Integer beer_id) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params=new WrapperParams(Wrappers.PRODUCT_EVALUATION);
                params.addParam(Keys.PRODUCT_MODEL, Keys.CAP_BEER);
                //params.addParam(Keys.PRODUCT_ID, beer_id);
                ListResponse<EvaluationBeer> listResponse= executeCall(getApi().getBeerEvaluation(params));
                subscriber.onNext(new ArrayList<>(listResponse.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
