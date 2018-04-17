package com.brewmapp.execution.task;

import com.brewmapp.data.entity.LocationChild;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 4/17/2018.
 */

public class CreateLocationTask extends BaseNetworkTask<WrapperParams, LocationChild> {

    @Inject
    public CreateLocationTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<LocationChild> prepareObservable(WrapperParams wrapperParams) {
        return Observable.create(subscriber -> {
            try {
//                WrapperParams wrapperParams = new WrapperParams(Wrappers.LOCATION);
//                wrapperParams.addParam(Keys.COUNTRY_ID,locationChild.getCountry_id());
//                wrapperParams.addParam(Keys.CITY_ID,locationChild.getCity_id());
//                wrapperParams.addParam(Keys.STREED,locationChild.getStreet());
//                wrapperParams.addParam(Keys.HOUSE,locationChild.getHouse());
//                wrapperParams.addParam(Keys.NAME,restoDetail_new_data.getResto().getName());
//                wrapperParams.addParam(Keys.LAT,locationChild.getLat());
//                wrapperParams.addParam(Keys.LON,locationChild.getLon());

                SingleResponse<LocationChild> response=executeCall(getApi().createLocation(wrapperParams));
//                locationChild.setId(response.getData().getId());
                subscriber.onNext(response.getData());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
