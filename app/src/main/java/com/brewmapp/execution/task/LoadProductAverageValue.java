package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Averagevalue;
import com.brewmapp.data.entity.container.InterestsByUser;
import com.brewmapp.data.pojo.LoadAverageValuePackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by Kras on 16.01.2018.
 */

public class LoadProductAverageValue extends BaseNetworkTask<LoadAverageValuePackage ,ListResponse<Averagevalue>> {

    @Inject
    public LoadProductAverageValue(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<ListResponse<Averagevalue>> prepareObservable(LoadAverageValuePackage loadAverageValuePackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams wrapperParams=new WrapperParams(Wrappers.AVERAGE_EVALUATION);
                wrapperParams.addParam(Keys.NAME,Keys.CAP_BEER);
                wrapperParams.addParam(Keys.ID,loadAverageValuePackage.getBeer_id());

                String key=new StringBuilder()
                        .append(getClass().toString())
                        .append(Keys.CAP_BEER)
                        .append(loadAverageValuePackage.getBeer_id())
                        .toString();
                ListResponse<Averagevalue>  o=null;
                if (loadAverageValuePackage.isCacheOn()) {
                    o = Paper.book().read(key);
                    if (o != null)
                        subscriber.onNext(o);
                }

                ListResponse<Averagevalue> listResponse=executeCall(getApi().loadProductAverageValue(wrapperParams));
                Paper.book().write(key,listResponse);
                if(o==null)
                    subscriber.onNext(listResponse);
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }
}
