package com.brewmapp.execution.task;

import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.BreweryTypes;
import com.brewmapp.data.entity.container.Breweries;
import com.brewmapp.data.pojo.ApiBreweryPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 02.02.2018.
 */

public class ApiBreweryTask extends BaseNetworkTask<ApiBreweryPackage, List<IFlexible>> {
    @Inject
    public ApiBreweryTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(ApiBreweryPackage apiBreweryPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Keys.CAP_BREWERY);
                if(apiBreweryPackage.getId()!=null)
                    params.addParam(Keys.ID,apiBreweryPackage.getId());
                if(apiBreweryPackage.getCountry_id()!=null)
                    params.addParam(Keys.COUNTRY_ID,apiBreweryPackage.getCountry_id());
                if(apiBreweryPackage.getLocation_id()!=null)
                    params.addParam(Keys.LOCATION_ID,apiBreweryPackage.getLocation_id());
                if(apiBreweryPackage.getBeer_country_id()!=null)
                    params.addParam(Keys.BEER_COUNTRY_ID,apiBreweryPackage.getBeer_country_id());
                if(apiBreweryPackage.getBeer_brand_id()!=null)
                    params.addParam(Keys.BEER_BRAND_ID,apiBreweryPackage.getBeer_brand_id());
                if(apiBreweryPackage.getBeer_type_id()!=null)
                    params.addParam(Keys.BEER_TYPE_ID,apiBreweryPackage.getBeer_type_id());

                Breweries o = executeCall(getApi().apiBrewery(params));
                subscriber.onNext(new ArrayList<>(o.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
