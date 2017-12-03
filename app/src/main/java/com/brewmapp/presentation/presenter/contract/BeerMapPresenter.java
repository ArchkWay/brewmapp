package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface BeerMapPresenter extends LivePresenter<BeerMapView> {
    void onLocationChanged(SimpleLocation location);
    void onGeocodeRequest(LatLng latLng);
    void onLoadedRestoGeo(int cityId);
    void onLoadedCity(String cityName);
    void loadRestoCoordinates(List<FilterRestoField> fieldList, int specialOffer);
    void loadBeerCoordinates(List<FilterBeerField> fieldList, int craftBeer);
}
