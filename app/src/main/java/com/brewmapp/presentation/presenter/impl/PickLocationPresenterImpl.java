package com.brewmapp.presentation.presenter.impl;

import android.location.Location;
import android.location.LocationManager;

import javax.inject.Inject;

import com.brewmapp.data.entity.BeerLocation;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.pojo.GeolocatorResultPackage;
import com.brewmapp.data.pojo.SearchPackage;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.LoadLocationTask;
import com.brewmapp.execution.task.SearchRestosTask;
import com.brewmapp.presentation.view.contract.PickLocationView;

import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.ovsa.tool.Geolocator;

import com.brewmapp.presentation.presenter.contract.PickLocationPresenter;
import com.google.android.gms.maps.model.LatLng;

public class PickLocationPresenterImpl extends BasePresenter<PickLocationView> implements PickLocationPresenter {

    private SearchRestosTask searchRestosTask;
    private SimpleLocation simpleLocation;
    private LoadLocationTask loadLocationTask;
    private Geolocator geolocator;

    @Inject
    public PickLocationPresenterImpl(SearchRestosTask searchRestosTask,
                                     LoadLocationTask loadLocationTask, Geolocator geolocator) {

        this.searchRestosTask = searchRestosTask;
        this.loadLocationTask = loadLocationTask;
        this.geolocator = geolocator;
    }

    @Override
    public void onDestroy() {
        searchRestosTask.cancel();
    }

    @Override
    public void onQuery(String query) {
        enableControls(false);
        SearchPackage searchPackage = new SearchPackage(query);
        if(simpleLocation != null) {
            searchPackage.setLat(simpleLocation.getLat());
            searchPackage.setLng(simpleLocation.getLng());
        }
        searchPackage.setStartLimit(0);
        searchPackage.setEndLimit(10);
        searchRestosTask.cancel();
        searchRestosTask.execute(searchPackage, new SimpleSubscriber<ListResponse<Resto>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showError(e.getMessage());
            }

            @Override
            public void onNext(ListResponse<Resto> restoListResponse) {
                enableControls(true);
                view.showVariants(restoListResponse.getModels());
            }
        });
    }

    @Override
    public void onLocationChanged(SimpleLocation location) {
        this.simpleLocation = location;
    }

    @Override
    public void onGeocodeRequest(LatLng latLng) {
        view.enableSearchPanel(false);
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        geolocator.geocodeAsync(location, string -> {
            view.enableSearchPanel(true);
            view.showGeolocationResult(new GeolocatorResultPackage(
                    string,
                    new SimpleLocation(
                            latLng.latitude, latLng.longitude
                    )
            ));
        });
    }

    @Override
    public void onSelectLocation(int id) {
        loadLocationTask.cancel();
        enableControls(false);
        loadLocationTask.execute(id, new SimpleSubscriber<BeerLocation.LocationInfo>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showError(e.getMessage());
            }

            @Override
            public void onNext(BeerLocation.LocationInfo info) {
                enableControls(true);
                view.showGeolocationResult(new GeolocatorResultPackage(
                        info.getFormattedAddress(),
                        new SimpleLocation(
                                info.getLat(), info.getLng()
                        )
                ));
            }
        });
    }
}
