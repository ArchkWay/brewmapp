package com.brewmapp.execution.task;

import com.brewmapp.data.pojo.SearchPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.LocationQueryParams;
import com.brewmapp.execution.exchange.response.base.ISearchResult;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import retrofit2.Call;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 30.09.17.
 */

public abstract class SearchTask<T> extends BaseNetworkTask<SearchPackage, T> {

    public SearchTask(MainThread mainThread,
                      Executor executor,
                      Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<T> prepareObservable(SearchPackage searchPackage) {
        return Observable.create(subscriber -> {
            try {
                T result = executeCall(prepareCall(prepareQueryParams(searchPackage), prepareFields(searchPackage)));
                subscriber.onNext(result);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    protected abstract Call<T> prepareCall(RequestParams queryParams, RequestParams fields);

    protected RequestParams prepareQueryParams(SearchPackage params) {
        LocationQueryParams queryParams = new LocationQueryParams(params.getLat(), params.getLng());
        if(params.getEndLimit() != 0) {
            queryParams.addParam(Keys.LIMIT_END, params.getEndLimit());
            queryParams.addParam(Keys.LIMIT_START, params.getStartLimit());
        }
        for(String key: params.getAdditionalQueryParams().keySet()) {
            queryParams.addParam(key, params.getAdditionalQueryParams().get(key));
        }

        return queryParams;
    }

    protected RequestParams prepareFields(SearchPackage params) {
        RequestParams requestParams = new RequestParams();
        for(String key: params.getAdditionalFields().keySet()) {
            requestParams.addParam(key, params.getAdditionalFields().get(key));
        }
        return requestParams;
    }
}
