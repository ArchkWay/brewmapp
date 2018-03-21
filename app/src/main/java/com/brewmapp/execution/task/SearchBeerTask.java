package com.brewmapp.execution.task;

import android.content.Context;

import com.brewmapp.R;
import com.brewmapp.app.environment.FilterKeys;
import com.brewmapp.data.entity.container.SearchBeerTypes;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by nixus on 07.12.2017.
 */

public class SearchBeerTask extends BaseNetworkTask<FullSearchPackage, List<IFlexible>> {

    private int step;

    @Inject
    public SearchBeerTask(MainThread mainThread, Executor executor, Api api, Context context) {
        super(mainThread, executor, api);
        this.step = context.getResources().getInteger(R.integer.step_items_load);;
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(FullSearchPackage fullSearchPackage) {
        return Observable.create(subscriber -> {
            try {
                RequestParams params = new RequestParams();
                int start = fullSearchPackage.getPage() * step;
                int end = start + step;
                //                params.addParam(FilterKeys.BEER_COUNTRY, beerPackage.getBeerCountries() != null ? beerPackage.getBeerCountries() : "");
                if(fullSearchPackage.getCountry()!=null)
                    params.addParam(FilterKeys.BEER_COUNTRY,fullSearchPackage.getCountry());
//                params.addParam(FilterKeys.BEER_TYPES, beerPackage.getBeerTypes() != null ? beerPackage.getBeerTypes() : "");
                if(fullSearchPackage.getType()!=null)
                    params.addParam(FilterKeys.BEER_TYPES,fullSearchPackage.getType());
                if(fullSearchPackage.getBeerBrand()!=null)
                    params.addParam(FilterKeys.BEER_BRAND,fullSearchPackage.getBeerBrand());
//                params.addParam(FilterKeys.BEER_POWER, beerPackage.getBeerStrengthes() != null ? beerPackage.getBeerStrengthes() : "");
                if(fullSearchPackage.getPower()!=null)
                    params.addParam(FilterKeys.BEER_POWER,fullSearchPackage.getPower());
//                params.addParam(FilterKeys.BEER_PACK, beerPackage.getBeerPacks() != null ? beerPackage.getBeerPacks() : "");
                if(fullSearchPackage.getPack()!=null)
                    params.addParam(FilterKeys.BEER_PACK,fullSearchPackage.getPack());
//                params.addParam(FilterKeys.BEER_BREWERIES, beerPackage.getBeerVolumes() != null ? beerPackage.getBeerVolumes() : "");
//                ??????????????????????? с фильтром BEER_BREWERIES ничего не находит
                if(fullSearchPackage.getBrewery()!=null)
                    params.addParam(FilterKeys.BEER_BREWERIES,fullSearchPackage.getBrewery());
                //params.addParam(FilterKeys.CRAFT , beerPackage.getCraft());
                //??????????????????????? нет FilterKeys.CRAFT в интерфейсе
//                params.addParam(FilterKeys.BEER_DENSITY, beerPackage.getBeerDensity() != null ? beerPackage.getBeerDensity() : "");
                //??????????????????????? API возвращает ошибку
                if(fullSearchPackage.getDensity()!=null)
                    params.addParam(FilterKeys.BEER_DENSITY,fullSearchPackage.getDensity());
//                params.addParam(FilterKeys.BEER_FILTETRED , beerPackage.getBeerFiltered());
                if(fullSearchPackage.getFitredBeer()!=null)
                    params.addParam(FilterKeys.BEER_FILTETRED,fullSearchPackage.getFitredBeer());
//                params.addParam(FilterKeys.BEER_DISCOUNT , beerPackage.getBeerDiscount());
                if(fullSearchPackage.getCity()!=null)
                    params.addParam(FilterKeys.BEER_CITY, fullSearchPackage.getCity());
                //??????????????????????? нет в интерфейсе
//                params.addParam(FilterKeys.BEER_VOLUMES, beerPackage.getBeerVolumes() != null ? beerPackage.getBeerVolumes() : "");                if(fullSearchPackage.getPack()!=null)
                //??????????????????????? нет в интерфейсе
//                params.addParam(FilterKeys.BEER_COLOR , beerPackage.getBeerColors() != null ? beerPackage.getBeerColors() : "");
                if(fullSearchPackage.getColor()!=null)
                    params.addParam(FilterKeys.BEER_COLOR ,fullSearchPackage.getColor());
//                params.addParam(FilterKeys.BEER_SMELL , beerPackage.getBeerFragrances() != null ? beerPackage.getBeerFragrances() : "");
                if(fullSearchPackage.getBeerFragrance()!=null)
                    params.addParam(FilterKeys.BEER_SMELL,fullSearchPackage.getBeerFragrance());
//                params.addParam(FilterKeys.BEER_TASTE , beerPackage.getBeerTastes() != null ? beerPackage.getBeerTastes() : "");
                if(fullSearchPackage.getTaste()!=null)
                    params.addParam(FilterKeys.BEER_TASTE,fullSearchPackage.getTaste());
//                params.addParam(FilterKeys.BEER_AFTER_TASTE , beerPackage.getBeerAftertastes() != null ? beerPackage.getBeerAftertastes() : "");
                if(fullSearchPackage.getAfterTaste()!=null)
                    params.addParam(FilterKeys.BEER_AFTER_TASTE ,fullSearchPackage.getAfterTaste());
//                params.addParam(FilterKeys.PRICE_BEER, beerPackage.getBeerAveragepriceRange() != null ? beerPackage.getBeerAveragepriceRange() : "");
                if(fullSearchPackage.getPrice()!=null)
                    params.addParam(FilterKeys.PRICE_BEER ,fullSearchPackage.getPrice());

//                params.addParam(FilterKeys.BEER_EVALUATION_TYPE, beerPackage.getProductEvaluationType() != null ? beerPackage.getProductEvaluationType() : "");

//
//                params.addParam(Keys.COORD_START , beerPackage.getCoordStart() != null ? beerPackage.getCoordStart() : "");
//                params.addParam(Keys.COORD_END , beerPackage.getCoordEnd() != null ? beerPackage.getCoordEnd() : "");
//                params.addParam(FilterKeys.BEER_IBU , beerPackage.getBeerIBU() != null ? beerPackage.getBeerIBU() : "");
                if(fullSearchPackage.getOrder()!=null)
                    params.addParam(Keys.ORDER_SORT_BEER,fullSearchPackage.getOrder());

                SearchBeerTypes response = executeCall(getApi().loadBeers(start , end, params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
