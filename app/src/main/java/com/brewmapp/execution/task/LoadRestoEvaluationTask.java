package com.brewmapp.execution.task;

import com.brewmapp.data.entity.EvaluationResto;
import com.brewmapp.data.pojo.EvaluationPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.common.ApiClient;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 11/3/2017.
 */

public class LoadRestoEvaluationTask extends BaseNetworkTask<EvaluationPackage, List<EvaluationResto>> {
    private ApiClient apiClient;

    @Inject
    public LoadRestoEvaluationTask(MainThread mainThread, Executor executor, Api api, ApiClient apiClient) {
        super(mainThread, executor, api);
        this.apiClient = apiClient;
    }

    @Override
    protected Observable<List<EvaluationResto>> prepareObservable(EvaluationPackage evaluationPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params=new WrapperParams(Wrappers.RESTO_EVALUATION);
                if(evaluationPackage.getModel_id()!=null)
                    params.addParam(Keys.RESTO_ID, evaluationPackage.getModel_id());

                ListResponse<EvaluationResto> listResponse;
                if(evaluationPackage.getToken()==null)
                    listResponse= executeCall(getApi().getRestoEvaluation(params));
                else {
                    apiClient.setTokenOffTemporarily();
                    listResponse = executeCall(getApi().getRestoEvaluationByToken(
                            evaluationPackage.getToken(),
                            params));
                }

                subscriber.onNext(listResponse.getModels());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
