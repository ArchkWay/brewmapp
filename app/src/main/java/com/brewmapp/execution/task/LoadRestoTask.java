package com.brewmapp.execution.task;

import android.util.Log;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.data.entity.container.Restos;
import com.brewmapp.data.pojo.LoadRestoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.utils.Cons;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 26.10.2017.
 */

public class LoadRestoTask extends BaseNetworkTask<LoadRestoPackage, Restos>  {

    @Inject
    public LoadRestoTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<Restos> prepareObservable(LoadRestoPackage loadRestoPackage) {
        return Observable.create(subscriber -> {
            ///api/resto/restodata?resto_id=64303
            try {

                Object restos= executeCall(getApi().getRestoDetails(loadRestoPackage.getId(),new WrapperParams("")));
                //subscriber.onNext(new ArrayList<Resto>(restos.getModels()));
                Log.i(Cons.COMOON_TAG,restos.toString());
                subscriber.onComplete();
            } catch (Exception e) {
                Log.i(Cons.COMOON_TAG,e.getMessage());
                subscriber.onError(e);
            }
        });
    }
}
