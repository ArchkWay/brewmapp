package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.data.pojo.RestoGeoPackage;
import com.brewmapp.execution.task.FilterBeerTask;
import com.brewmapp.execution.task.FilterRestoTask;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.execution.task.LoadRestoGeoTask;
import com.brewmapp.execution.task.LoadRestoLocationTask;
import com.brewmapp.execution.task.SearchOnMapTask;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.view.contract.BeerMapView;

import java.util.ArrayList;
import java.util.Iterator;
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

public class BeerMapPresenterImpl extends BasePresenter<BeerMapView> implements BeerMapPresenter {

    private SimpleLocation simpleLocation;
    private Geolocator geolocator;

    private LoadRestoLocationTask loadRestoLocationTask;
    private LoadCityTask loadCityTask;
    private FilterRestoTask filterRestoTask;
    private FilterBeerTask filterBeerTask;
    private SearchOnMapTask searchOnMapTask;
    private LoadRestoGeoTask loadRestoGeoTask;

    @Inject
    public BeerMapPresenterImpl(LoadCityTask loadCityTask,
                                LoadRestoLocationTask loadRestoLocationTask,
                                FilterRestoTask filterRestoTask,
                                FilterBeerTask filterBeerTask,
                                SearchOnMapTask searchOnMapTask,
                                LoadRestoGeoTask loadRestoGeoTask) {

        this.loadRestoLocationTask = loadRestoLocationTask;
        this.loadCityTask = loadCityTask;
        this.filterRestoTask = filterRestoTask;
        this.filterBeerTask = filterBeerTask;
        this.searchOnMapTask = searchOnMapTask;
        this.loadRestoGeoTask = loadRestoGeoTask;
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
    public void onLoadedRestoGeo(int id) {
        loadRestoLocationTask.cancel();
        loadRestoLocationTask.execute(id, new SimpleSubscriber<List<FilterRestoLocation>>() {
            @Override
            public void onError(Throwable e) {
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<FilterRestoLocation> restoLocation) {
                view.addRestoToMap(restoLocation);
            }
        });
    }

    @Override
    public void sendQueryRestoSearch(FullSearchPackage fullSearchPackage) {
        searchOnMapTask.cancel();
        searchOnMapTask.execute(fullSearchPackage, new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.hideProgressBar();
                view.appendItems(iFlexibles);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgressBar();
                view.showMessage(e.getMessage(),0);
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
                //view.appendItems(new ArrayList<>(restoLocations));
                ArrayList arrayList=new ArrayList<>();
                Iterator<FilterRestoLocationInfo> infoIterator=restoLocations.iterator();

                while (infoIterator.hasNext())
                    arrayList.add(new FilterRestoLocation(infoIterator.next().getModel()));
                view.addRestoToMap(arrayList);
                view.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.hideProgressBar();
            }
        });
    }


}

