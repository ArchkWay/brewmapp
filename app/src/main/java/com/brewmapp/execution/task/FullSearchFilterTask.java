package com.brewmapp.execution.task;

import android.content.Context;

import com.brewmapp.R;
import com.brewmapp.data.entity.container.BeerBrands;
import com.brewmapp.data.entity.container.Breweries;
import com.brewmapp.data.entity.container.FilterBeer;
import com.brewmapp.data.entity.container.Restos;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 17.11.2017.
 */

public class FullSearchFilterTask extends BaseNetworkTask<FullSearchPackage, List<IFlexible>> {

    private int step;

    @Inject
    public FullSearchFilterTask(MainThread mainThread, Executor executor, Api api, Context context) {
        super(mainThread, executor, api);
        this.step = context.getResources().getInteger(R.integer.step_items_load);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.SEARCH_TYPE);
                params.addParam(Keys.TYPE, fullSearchPackage.getType());
                int start = fullSearchPackage.getPage() * step;
                int end = start + step;
                switch (fullSearchPackage.getType()) {
                    case Keys.TYPE_BEER:
                        FilterBeer filterBeer = executeCall(getApi().filterSearchBeer(fullSearchPackage.getStringSearch(), start, end, params));
                        subscriber.onNext(new ArrayList<>(filterBeer.getModels()));
                        subscriber.onComplete();
                        break;
                    case Keys.TYPE_RESTO:
                        Restos restos = executeCall(getApi().fullSearchResto(fullSearchPackage.getStringSearch(), start, end, params));
                        subscriber.onNext(new ArrayList<>(restos.getModels()));
                        subscriber.onComplete();
                        break;
                    case Keys.TYPE_BREWERY:
                        Breweries breweries = executeCall(getApi().fullSearchBrewery(fullSearchPackage.getStringSearch(), start, end, params));
                        subscriber.onNext(new ArrayList<>(breweries.getModels()));
                        subscriber.onComplete();
                        break;
                    case Keys.TYPE_BEERBRAND:
                        BeerBrands beerBrands= executeCall(getApi().fullSearchBeerBrand(fullSearchPackage.getStringSearch(), start, end, params));
                        subscriber.onNext(new ArrayList<>(beerBrands.getModels()));
                        subscriber.onComplete();
                        break;
                }
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}