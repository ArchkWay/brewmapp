package com.brewmapp.presentation.view.contract;

import android.location.Location;

import com.brewmapp.data.entity.City;

import ru.frosteye.ovsa.execution.executor.Callback;

/**
 * Created by Kras on 20.02.2018.
 */

public interface OnLocationInteractionListener {
    void requestLastLocation(Callback<Location> callback);
    void requestCity(Callback<City> callback);
    void requestRefreshLocation();
    Location getDefaultLocation();

}
