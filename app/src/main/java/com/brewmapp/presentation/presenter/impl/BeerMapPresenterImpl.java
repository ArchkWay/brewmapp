package com.brewmapp.presentation.presenter.impl;

import android.location.Location;
import android.location.LocationManager;

import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.pojo.FilterBeerPackage;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.task.FilterBeerTask;
import com.brewmapp.execution.task.FilterRestoTask;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.execution.task.LoadLocationTask;
import com.brewmapp.execution.task.LoadRestoLocationTask;
import com.brewmapp.execution.task.SearchOnMapTask;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.google.android.gms.maps.model.LatLng;

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

    @Inject
    public BeerMapPresenterImpl(LoadCityTask loadCityTask,
                                LoadRestoLocationTask loadRestoLocationTask,
                                FilterRestoTask filterRestoTask,
                                FilterBeerTask filterBeerTask,
                                SearchOnMapTask searchOnMapTask) {

        this.loadRestoLocationTask = loadRestoLocationTask;
        this.loadCityTask = loadCityTask;
        this.filterRestoTask = filterRestoTask;
        this.filterBeerTask = filterBeerTask;
        this.searchOnMapTask = searchOnMapTask;
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
    public void onLocationChanged(SimpleLocation location) {
        loadRestoLocationTask.cancel();
        loadCityTask.cancel();
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
        loadRestoLocationTask.execute(id, new SimpleSubscriber<List<FilterRestoLocation>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<FilterRestoLocation> restoLocation) {
                enableControls(true);
                view.showGeolocationResult(restoLocation);
            }
        });
    }

    @Override
    public void onLoadedCity(String cityName) {
        loadCityTask.cancel();
        enableControls(false);
        GeoPackage geoPackage = new GeoPackage();
        geoPackage.setCityName(cityName);
        loadCityTask.execute(geoPackage, new SimpleSubscriber<List<City>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<City> restoLocation) {
                onLoadedRestoGeo(restoLocation.get(0).getId());
                enableControls(true);
            }
        });
    }

    @Override
    public void loadRestoCoordinates(List<FilterRestoField> filterRestoFields, int specialOffer) {
        filterRestoTask.cancel();
        FilterRestoPackage filterRestoPackage = new FilterRestoPackage();
        filterRestoPackage.setRestoCity(filterRestoFields.get(FilterRestoField.CITY).getSelectedItemId());
        filterRestoPackage.setRestoTypes(filterRestoFields.get(FilterRestoField.TYPE).getSelectedItemId());
        filterRestoPackage.setMenuBeer(filterRestoFields.get(FilterRestoField.BEER).getSelectedItemId());
        filterRestoPackage.setRestoKitchens(filterRestoFields.get(FilterRestoField.KITCHEN).getSelectedItemId());
        filterRestoPackage.setRestoAveragepriceRange(filterRestoFields.get(FilterRestoField.PRICE).getSelectedItemId());
        filterRestoPackage.setRestoFeatures(filterRestoFields.get(FilterRestoField.FEATURES).getSelectedItemId());
        filterRestoPackage.setResto_discount(specialOffer);
        filterRestoTask.execute(filterRestoPackage, new SimpleSubscriber<List<FilterRestoLocation>>() {
            @Override
            public void onError(Throwable e) {
                showError(e.getMessage());
                view.showDialogProgressBar(false);
            }

            @Override
            public void onNext(List<FilterRestoLocation> restoLocations) {
                view.showDialogProgressBar(false);
                if (restoLocations.size() == 0) {
                    view.showMessage("Не найдено ни одного заведения",0);
                } else {
                    view.showGeolocationResult(restoLocations);
                }
            }

            @Override
            public void onComplete() {
                view.showDialogProgressBar(false);
                super.onComplete();
            }
        });
    }

    @Override
    public void loadBeerCoordinates(List<FilterBeerField> fieldList, int craftBeer) {
        filterBeerTask.cancel();
        FilterBeerPackage filterBeerPackage = new FilterBeerPackage();
        filterBeerPackage.setBeerCountries(fieldList.get(FilterBeerField.COUNTRY).getSelectedItemId());
        filterBeerPackage.setBeerTypes(fieldList.get(FilterBeerField.TYPE).getSelectedItemId());
        filterBeerPackage.setBeerStrengthes(fieldList.get(FilterBeerField.POWER).getSelectedItemId());
        filterBeerPackage.setBeerPacks(fieldList.get(FilterBeerField.BEER_PACK).getSelectedItemId());
        filterBeerPackage.setBeerBreweries(fieldList.get(FilterBeerField.BREWERY).getSelectedItemId());
        filterBeerPackage.setBeerCity(fieldList.get(FilterBeerField.PLACE).getSelectedItemId());
        filterBeerPackage.setCraft(craftBeer);
        filterBeerPackage.setBeerDensity(fieldList.get(FilterBeerField.DENSITY).getSelectedItemId());
//        filterBeerPackage.setBeerFiltered(fieldList.get(FilterBeerField.COUNTRY).getSelectedItemId());

        filterBeerPackage.setBeerAveragepriceRange(fieldList.get(FilterBeerField.PRICE_BEER).getSelectedItemId());
        filterBeerPackage.setBeerColors(fieldList.get(FilterBeerField.COLOR).getSelectedItemId());
        filterBeerPackage.setBeerFragrances(fieldList.get(FilterBeerField.SMELL).getSelectedItemId());
        filterBeerPackage.setBeerTastes(fieldList.get(FilterBeerField.TASTE).getSelectedItemId());
        filterBeerPackage.setBeerAftertastes(fieldList.get(FilterBeerField.AFTER_TASTE).getSelectedItemId());
        filterBeerPackage.setBeerAftertastes(fieldList.get(FilterBeerField.BREWERY).getSelectedItemId());
        filterBeerPackage.setBeerIBU(fieldList.get(FilterBeerField.IBU).getSelectedItemId());
        filterBeerTask.execute(filterBeerPackage, new SimpleSubscriber<List<FilterRestoLocation>>() {
            @Override
            public void onError(Throwable e) {
                view.showDialogProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<FilterRestoLocation> restoLocations) {
                view.showDialogProgressBar(false);
                if (restoLocations.size() == 0) {
                    view.showMessage("Не найдено ни одного заведения",0);
                } else {
                    view.showGeolocationResult(restoLocations);
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                view.showDialogProgressBar(false);
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

}

