package com.brewmapp.data.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by oleg on 25.09.17.
 */

public interface ILocation {
    LatLng position();
    String title();
}
