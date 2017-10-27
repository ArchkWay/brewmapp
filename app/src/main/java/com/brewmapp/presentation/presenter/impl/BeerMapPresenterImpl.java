package com.brewmapp.presentation.presenter.impl;

import android.location.Location;
import android.location.LocationManager;

import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.execution.task.LoadLocationTask;
import com.brewmapp.execution.task.LoadRestoLocationTask;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.ovsa.tool.Geolocator;

/**
 * Created by ovcst on 24.08.2017.
 */

public class BeerMapPresenterImpl extends BasePresenter<BeerMapView> implements BeerMapPresenter {

    private SimpleLocation simpleLocation;
    //    private LoadLocationTask loadLocationTask;
    private Geolocator geolocator;

    private LoadRestoLocationTask loadRestoLocationTask;
    private LoadCityTask loadCityTask;

    @Inject
    public BeerMapPresenterImpl(LoadLocationTask loadLocationTask,
                                LoadCityTask loadCityTask,
                                LoadRestoLocationTask loadRestoLocationTask) {
        this.loadRestoLocationTask = loadRestoLocationTask;
        this.loadCityTask = loadCityTask;
//        this.loadLocationTask = loadLocationTask;
    }

    @Override
    public void onDestroy() {
        loadRestoLocationTask.cancel();
        loadCityTask.cancel();
    }

    @Override
    public void onLocationChanged(SimpleLocation location) {
        this.simpleLocation = location;
    }

    @Override
    public void onGeocodeRequest(LatLng latLng) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
    }

    @Override
    public void onLoadedRestoGeo(int id) {
        loadRestoLocationTask.cancel();
        enableControls(false);
        loadRestoLocationTask.execute(id, new SimpleSubscriber<List<RestoLocation>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<RestoLocation> restoLocation) {
                enableControls(true);
                view.showGeolocationResult(restoLocation);
            }
        });
    }

    @Override
    public void onLoadedCity() {
        loadCityTask.cancel();
        enableControls(false);
        loadCityTask.execute(null, new SimpleSubscriber<List<City>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<City> restoLocation) {
                enableControls(true);
            }
        });
    }
}

