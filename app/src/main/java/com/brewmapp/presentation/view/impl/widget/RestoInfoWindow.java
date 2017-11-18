package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
import android.graphics.Typeface;
import android.location.Location;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by nlbochas on 22/10/2017.
 */

public class RestoInfoWindow implements GoogleMap.InfoWindowAdapter {
    private final View markerView;
    private final Location myLocation;

    public RestoInfoWindow(Activity activity, Location myLocation) {
        this.myLocation = myLocation;
        markerView = activity.getLayoutInflater()
                .inflate(R.layout.layout_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, markerView, myLocation);
        return markerView;
    }

    public View getInfoContents(Marker marker) {
        return null;
    }

    private void render(Marker marker, View view, Location myLocation) {
        TextView restoTitle = ((TextView) view.findViewById(R.id.title));
        TextView distance = ((TextView) view.findViewById(R.id.distance));
        restoTitle.setTypeface(null, Typeface.BOLD_ITALIC);
        restoTitle.setText(marker.getTitle());
        distance.setText(calculateDistance(marker, myLocation));
    }

    private String calculateDistance(Marker marker, Location myLocation) {
        float[] distances = new float[1];
        Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
                marker.getPosition().latitude, marker.getPosition().longitude,
                distances);
        return String.valueOf(distances[0]);
    }

}