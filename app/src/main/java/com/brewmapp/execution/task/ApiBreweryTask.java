package com.brewmapp.execution.task;

import android.content.Context;
import android.content.res.Resources;

import com.brewmapp.R;
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.BreweryTypes;
import com.brewmapp.data.entity.container.Breweries;
import com.brewmapp.data.pojo.ApiBreweryPackage;
import com.brewmapp.data.pojo.FullSearchPackage;
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

public class ApiBreweryTask extends BaseNetworkTask<FullSearchPackage, List<IFlexible>> {

    private int step = 0;

    @Inject
    public ApiBreweryTask(MainThread mainThread, Executor executor, Api api, Context context) {
        super(mainThread, executor, api);
        step = context.getResources().getInteger(R.integer.step_items_load);
    }


    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                int start = step*fullSearchPackage.getPage();
                int end = start+step;

                WrapperParams params = new WrapperParams(Keys.CAP_BREWERY);

                if(fullSearchPackage.getId()!=null)
                    params.addParam(Keys.ID,fullSearchPackage.getId());
                else
                    params.addParam(Keys.ID,"");
                if(fullSearchPackage.getCountry()!=null)
                    params.addParam(Keys.COUNTRY_ID,fullSearchPackage.getCountry());
//                if(apiBreweryPackage.getCountry_id()!=null)
//                    params.addParam(Keys.COUNTRY_ID,apiBreweryPackage.getCountry_id());
//                if(apiBreweryPackage.getLocation_id()!=null)
//                    params.addParam(Keys.LOCATION_ID,apiBreweryPackage.getLocation_id());
//                if(apiBreweryPackage.getBeer_country_id()!=null)
//                    params.addParam(Keys.BEER_COUNTRY_ID,apiBreweryPackage.getBeer_country_id());
                if(fullSearchPackage.getBeerBrand()!=null)
                    params.addParam(Keys.BEER_BRAND_ID,fullSearchPackage.getBeerBrand());
                if(fullSearchPackage.getType()!=null)
                    params.addParam(Keys.BEER_TYPE_ID,fullSearchPackage.getType());

                Breweries o = executeCall(getApi().apiBrewery(start,end,params));
                subscriber.onNext(new ArrayList<>(o.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
