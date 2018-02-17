package com.brewmapp.execution.task;

import android.content.Context;

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
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 08.12.2017.
 */

public class SearchBreweryTask extends BaseNetworkTask<BreweryPackage, List<IFlexible>> {

    private int step;

    @Inject
    public SearchBreweryTask(MainThread mainThread, Executor executor, Api api, Context context) {
        super(mainThread, executor, api);
        this.step = context.getResources().getInteger(R.integer.step_items_load);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BreweryPackage breweryPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("Brewery");
                int end = breweryPackage.getPage() + step;
                params.addParam(FilterKeys.BREWERY_COUNTRY, breweryPackage.getCountryId() != null ? breweryPackage.getCountryId() : "");
                params.addParam(FilterKeys.BREWERY_BEER_BRAND, breweryPackage.getBeerBrandId() != null ? breweryPackage.getBeerBrandId() : "");
                params.addParam(FilterKeys.BREWERY_BEER_TYPE, breweryPackage.getBeerTypeId() != null ? breweryPackage.getBeerTypeId() : "");
                BreweryTypes response = executeCall(getApi().loadBrewery(breweryPackage.getPage(), end, params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
