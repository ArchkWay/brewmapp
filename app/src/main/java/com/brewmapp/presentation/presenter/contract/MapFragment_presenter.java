package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.presentation.view.contract.MapFragment_view;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface MapFragment_presenter extends LivePresenter<MapFragment_view> {

    void sendQueryRestoSearch(FullSearchPackage fullSearchPackage);

    void loadRestoByLatLngBounds(GeoPackage geoPackage);

}
