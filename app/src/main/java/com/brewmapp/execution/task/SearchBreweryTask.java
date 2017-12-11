package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.data.entity.BreweryTypes;
import com.brewmapp.data.pojo.BreweryPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 08.12.2017.
 */

public class SearchBreweryTask extends BaseNetworkTask<BreweryPackage, List<IFlexible>> {

    private int step;

    @Inject
    public SearchBreweryTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
        this.step = ResourceHelper.getInteger(R.integer.config_posts_pack_size);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BreweryPackage beerPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("Brewery");
                int start = beerPackage.getPage() * step;
                int end = beerPackage.getPage() * step + step;
                params.addParam(FilterKeys.BREWERY_COUNTRY, beerPackage.getCountryId() != null ? beerPackage.getCountryId() : "");
                params.addParam(FilterKeys.BREWERY_BEER_BRAND, beerPackage.getBeerBrandId() != null ? beerPackage.getBeerBrandId() : "");
                params.addParam(FilterKeys.BREWERY_BEER_TYPE, beerPackage.getBeerTypeId() != null ? beerPackage.getBeerTypeId() : "");
                BreweryTypes response = executeCall(getApi().loadBrewery(params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
