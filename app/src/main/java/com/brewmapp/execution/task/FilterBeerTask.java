package com.brewmapp.execution.task;

import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.pojo.FilterBeerPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 02.12.2017.
 */

public class FilterBeerTask extends BaseNetworkTask<FilterBeerPackage, List<FilterRestoLocation>> {

    @Inject
    public FilterBeerTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<FilterRestoLocation>> prepareObservable(FilterBeerPackage beerPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("");
                params.addParam(FilterKeys.BEER_COUNTRY, beerPackage.getBeerCountries() != null ? beerPackage.getBeerCountries() : "");
                params.addParam(FilterKeys.BEER_TYPES, beerPackage.getBeerTypes() != null ? beerPackage.getBeerTypes() : "");
                params.addParam(FilterKeys.BEER_POWER, beerPackage.getBeerStrengthes() != null ? beerPackage.getBeerStrengthes() : "");
                params.addParam(FilterKeys.BEER_VOLUMES, beerPackage.getBeerVolumes() != null ? beerPackage.getBeerVolumes() : "");
                params.addParam(FilterKeys.BEER_BREWERIES, beerPackage.getBeerVolumes() != null ? beerPackage.getBeerVolumes() : "");
                params.addParam(FilterKeys.CRAFT , beerPackage.getCraft());
                params.addParam(FilterKeys.BEER_DENSITY, beerPackage.getBeerDensity() != null ? beerPackage.getBeerDensity() : "");
                params.addParam(FilterKeys.BEER_FILTETRED , beerPackage.getBeerFiltered());
                params.addParam(FilterKeys.BEER_DISCOUNT , beerPackage.getBeerDiscount());
                params.addParam(FilterKeys.BEER_CITY, beerPackage.getBeerCity() != null ? beerPackage.getBeerCity() : "");

                params.addParam(FilterKeys.BEER_PACK, beerPackage.getBeerPacks() != null ? beerPackage.getBeerPacks() : "");
                params.addParam(FilterKeys.PRICE_BEER, beerPackage.getBeerAveragepriceRange() != null ? beerPackage.getBeerAveragepriceRange() : "");
                params.addParam(FilterKeys.BEER_EVALUATION_TYPE, beerPackage.getProductEvaluationType() != null ? beerPackage.getProductEvaluationType() : "");
                params.addParam(FilterKeys.BEER_COLOR , beerPackage.getBeerColors() != null ? beerPackage.getBeerColors() : "");

                params.addParam(FilterKeys.BEER_POWER , beerPackage.getBeerColors() != null ? beerPackage.getBeerColors() : "");
                params.addParam(FilterKeys.BEER_SMELL , beerPackage.getBeerFragrances() != null ? beerPackage.getBeerFragrances() : "");
                params.addParam(FilterKeys.BEER_TASTE , beerPackage.getBeerTastes() != null ? beerPackage.getBeerTastes() : "");

                params.addParam(FilterKeys.BEER_AFTER_TASTE , beerPackage.getBeerAftertastes() != null ? beerPackage.getBeerAftertastes() : "");
                params.addParam(Keys.COORD_START , beerPackage.getCoordStart() != null ? beerPackage.getCoordStart() : "");
                params.addParam(Keys.COORD_END , beerPackage.getCoordEnd() != null ? beerPackage.getCoordEnd() : "");
                params.addParam(FilterKeys.BEER_IBU , beerPackage.getBeerIBU() != null ? beerPackage.getBeerIBU() : "");

                ListResponse<FilterRestoLocation> response = executeCall(getApi().loadBeerLocation(params));

                subscriber.onNext(response.getModels());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
