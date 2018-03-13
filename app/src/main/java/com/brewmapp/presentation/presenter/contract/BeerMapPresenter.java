package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.presentation.view.contract.BeerMapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface BeerMapPresenter extends LivePresenter<BeerMapView> {

    void sendQueryRestoSearch(FullSearchPackage fullSearchPackage);

    void loadRestoByLatLngBounds(GeoPackage geoPackage);

}
