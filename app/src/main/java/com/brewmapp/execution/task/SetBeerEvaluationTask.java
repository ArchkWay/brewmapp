package com.brewmapp.execution.task;

import com.brewmapp.data.pojo.EvaluationPackage;
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
 * Created by Kras on 21.01.2018.
 */

public class SetBeerEvaluationTask extends BaseNetworkTask<EvaluationPackage, String> {

    @Inject
    public SetBeerEvaluationTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<String> prepareObservable(EvaluationPackage evaluationPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params=new WrapperParams(Wrappers.PRODUCT_EVALUATION);
                if(evaluationPackage.getEaluation_value()!=null) {
                    params.addParam(Keys.PRODUCT_MODEL, Keys.CAP_BEER);
                    params.addParam(Keys.PRODUCT_ID, evaluationPackage.getModel_id());
                    params.addParam(Keys.EVLUATION_TYPE, evaluationPackage.getEaluation_type());
                    params.addParam(Keys.EVLUATION_VALUE, evaluationPackage.getEaluation_value());
                    Object o = executeCall(getApi().setBeerEvaluation(params));
                }
                subscriber.onNext("");
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
