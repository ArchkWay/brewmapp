package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.PickLocationView;
import com.google.android.gms.maps.model.LatLng;

import ru.frosteye.ovsa.data.entity.SimpleLocation;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface PickLocationPresenter extends LivePresenter<PickLocationView> {
    void onQuery(String query);
    void onLocationChanged(SimpleLocation location);
    void onGeocodeRequest(LatLng latLng);
    void onSelectLocation(int id);
}
