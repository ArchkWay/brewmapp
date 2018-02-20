package com.brewmapp.presentation.view.contract;

import android.location.Location;

import ru.frosteye.ovsa.execution.executor.Callback;

/**
 * Created by Kras on 20.02.2018.
 */

public interface OnLocationInteractionListener {
    void getLocation(Callback<Location> callback);
}
