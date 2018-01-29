package com.brewmapp.execution.task;


import android.util.Log;

import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 26.10.2017.
 */

public class LoadRestoDetailTask extends BaseNetworkTask<LoadRestoDetailPackage, RestoDetail>  {

    @Inject
    public LoadRestoDetailTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<RestoDetail> prepareObservable(LoadRestoDetailPackage loadRestoDetailPackage) {
        return Observable.create(subscriber -> {
            try {

                String key=new StringBuilder()
                        .append(getClass().toString())
                        .append(loadRestoDetailPackage.getId())
                        .toString();
                RestoDetails  o= Paper.book().read(key);
                if(o!=null) {
                    subscriber.onNext((RestoDetail) o.getModels().get(0).getModel());
                    Log.i("NetworkTask","LoadRestoDetailTask - cache-read");
                }

                RestoDetails restoDetails= executeCall(getApi().getRestoDetails(loadRestoDetailPackage.getId(),new WrapperParams("")));
                Paper.book().write(key,restoDetails);
                Log.i("NetworkTask","LoadRestoDetailTask - cache-write");
                if(o==null)
                    subscriber.onNext((RestoDetail) restoDetails.getModels().get(0).getModel());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
