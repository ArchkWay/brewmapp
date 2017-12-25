package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by nlbochas on 22/10/2017.
 */

public class RestoInfoWindow implements GoogleMap.InfoWindowAdapter {
    private final View markerView;
    private final Activity activity;

    public RestoInfoWindow(Activity activity) {
        this.activity = activity;
        markerView = activity.getLayoutInflater()
                .inflate(R.layout.layout_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, markerView);
        return markerView;
    }

    public View getInfoContents(Marker marker) {
        return null;
    }

    private void render(Marker marker, View view) {
        LocationManager service = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        TextView restoTitle = ((TextView) view.findViewById(R.id.title));
        TextView cityName = ((TextView) view.findViewById(R.id.city));
        restoTitle.setTypeface(null, Typeface.BOLD_ITALIC);
        restoTitle.setText(marker.getTitle());
        cityName.setText(activity.getResources().getString(R.string.city, MapUtils.getCityName(location, activity)));
    }
}