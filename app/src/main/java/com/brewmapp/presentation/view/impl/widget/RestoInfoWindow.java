package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
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

    public RestoInfoWindow(Activity activity) {
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
        TextView tvTitle = ((TextView) view.findViewById(R.id.title));
        TextView tvSnippet = ((TextView) view.findViewById(R.id.snippet));
        tvTitle.setText(marker.getTitle());
    }

}