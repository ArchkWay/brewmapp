package ru.frosteye.beermap.execution.task.base;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.ovsa.data.storage.Repo;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.base.NetworkTask;

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
                    if(cachedFirst()) processCached(subscriber);
                    R result = executeCall(getCall(p));
                    getRepo().save(result);
                    subscriber.onNext(result);
                    subscriber.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    if(!cachedFirst()) processCached(subscriber);
                    subscriber.onError(e);
                }
            }
        });
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
