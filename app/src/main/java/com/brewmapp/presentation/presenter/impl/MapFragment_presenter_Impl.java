package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.data.pojo.RestoGeoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.FilterBeerTask;
import com.brewmapp.execution.task.FilterRestoTask;
import com.brewmapp.execution.task.FullSearchFilterTask;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.execution.task.LoadRestoGeoTask;
import com.brewmapp.execution.task.LoadRestoLocationTask;
import com.brewmapp.execution.task.SearchOnMapTask;
import com.brewmapp.presentation.presenter.contract.MapFragment_presenter;
import com.brewmapp.presentation.view.contract.MapFragment_view;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.ovsa.tool.Geolocator;

/**
 * Created by ovcst on 24.08.2017.
 */

public class MapFragment_presenter_Impl extends BasePresenter<MapFragment_view> implements MapFragment_presenter {

    private SimpleLocation simpleLocation;
    private Geolocator geolocator;

    private LoadRestoLocationTask loadRestoLocationTask;
    private LoadCityTask loadCityTask;
    private FilterRestoTask filterRestoTask;
    private FilterBeerTask filterBeerTask;
    private SearchOnMapTask searchOnMapTask;
    private FullSearchFilterTask fullSearchFilterTask;
    private LoadRestoGeoTask loadRestoGeoTask;

    @Inject
    public MapFragment_presenter_Impl(LoadCityTask loadCityTask,
                                      LoadRestoLocationTask loadRestoLocationTask,
                                      FilterRestoTask filterRestoTask,
                                      FilterBeerTask filterBeerTask,
                                      SearchOnMapTask searchOnMapTask,
                                      LoadRestoGeoTask loadRestoGeoTask,
                                      FullSearchFilterTask fullSearchFilterTask) {

        this.loadRestoLocationTask = loadRestoLocationTask;
        this.loadCityTask = loadCityTask;
        this.filterRestoTask = filterRestoTask;
        this.filterBeerTask = filterBeerTask;
        this.searchOnMapTask = searchOnMapTask;
        this.loadRestoGeoTask = loadRestoGeoTask;
        this.fullSearchFilterTask = fullSearchFilterTask;
    }

    @Override
    public void onDestroy() {
        loadRestoLocationTask.cancel();
        loadCityTask.cancel();
        filterRestoTask.cancel();
        filterBeerTask.cancel();
        searchOnMapTask.cancel();
    }


    @Override
    public void sendQueryRestoSearch(FullSearchPackage fullSearchPackage) {
        fullSearchFilterTask.cancel();
        fullSearchPackage.setType(Keys.TYPE_RESTO);
        fullSearchFilterTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                view.hideProgressBar();
                view.appendItems(iFlexibles);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgressBar();
            }
        });
    }

    @Override
    public void loadRestoByLatLngBounds(GeoPackage geoPackage) {
        loadRestoGeoTask.cancel();
        RestoGeoPackage restoGeoPackage=new RestoGeoPackage();
        restoGeoPackage.setCoordStart(geoPackage.getCoordStart());
        restoGeoPackage.setCoordEnd(geoPackage.getCoordEnd());
        view.showProgressBar();
        loadRestoGeoTask.execute(restoGeoPackage,new SimpleSubscriber<List<FilterRestoLocationInfo>>(){
            @Override
            public void onNext(List<FilterRestoLocationInfo> restoLocations) {
                super.onNext(restoLocations);
                view.addRestoToMap(restoLocations);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgressBar();
            }
        });
    }


}

