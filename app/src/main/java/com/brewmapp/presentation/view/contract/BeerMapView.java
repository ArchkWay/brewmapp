package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.RestoLocation;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface BeerMapView extends BasicView {
    void showGeolocationResult(List<RestoLocation> geolocatorResultPackageList);
}
