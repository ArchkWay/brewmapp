package com.brewmapp.execution.task.base;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import com.brewmapp.execution.exchange.common.Api;
import ru.frosteye.ovsa.data.storage.Repo;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by ovcst on 06.04.2017.
 */

public abstract class LoaderTask<P, R> extends BaseNetworkTask<P, R> {

    
    public LoaderTask(MainThread mainThread,
                      Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<R> prepareObservable(final P p) {
        return Observable.create(new ObservableOnSubscribe<R>() {
            @Override
            public void subscribe(
                    @io.reactivex.annotations.NonNull ObservableEmitter<R> subscriber) throws Exception {
                try {
                    if(cachedFirst() && !disableCache()) processCached(subscriber);
                    R result = executeCall(getCall(p));
                    getRepo().save(result);
                    subscriber.onNext(result);
                    subscriber.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    if(!cachedFirst() && !disableCache()) processCached(subscriber);
                    subscriber.onError(e);
                }
            }
        });
    }

    protected boolean disableCache() {
        return false;
    }

    private void processCached(ObservableEmitter<R> subscriber) {
        R cachedResult = getRepo().load();
        if(cachedResult != null)
            subscriber.onNext(cachedResult);
    }

    protected boolean cachedFirst() {
        return false;
    }

    @NonNull
    protected abstract Repo<R> getRepo();

    @NonNull
    protected abstract Call<R> getCall(P params);
}
