package com.brewmapp.execution.task;

import android.util.Log;

import com.brewmapp.data.entity.AverageEvaluation;
import com.brewmapp.data.pojo.RestoAverageEvaluationPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 11/7/2017.
 */

public class LoadRestoAverageEvaluationTask extends BaseNetworkTask<RestoAverageEvaluationPackage, List<AverageEvaluation>> {

    @Inject
    public LoadRestoAverageEvaluationTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<AverageEvaluation>> prepareObservable(RestoAverageEvaluationPackage restoAverageEvaluationPackage) {
        return  Observable.create(subscriber -> {
            try {
                WrapperParams params=new WrapperParams(Wrappers.AVERAGE_EVALUATION);
                params.addParam(Keys.ID, restoAverageEvaluationPackage.getResto_id());

                ListResponse<AverageEvaluation> averageEvaluationListResponse= executeCall(getApi().getRestoAverageEvaluation(params));
                subscriber.onNext(averageEvaluationListResponse.getModels());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
