package com.brewmapp.utils.events.markerCluster;

import android.content.Context;

import com.brewmapp.data.entity.FilterRestoLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Set;

/**
 * Created by nixus on 02.12.2017.
 */

public class ClusterRender extends DefaultClusterRenderer<FilterRestoLocation> {
    GoogleMap mMap;
    public ClusterRender(Context context, GoogleMap map,
                         ClusterManager<FilterRestoLocation> clusterManager) {
        super(context, map, clusterManager);
        this.mMap = map;
    }


    protected void onBeforeClusterItemRendered(FilterRestoLocation item, MarkerOptions markerOptions) {

        markerOptions.icon(item.getIcon());
        markerOptions.snippet(item.getRestoId());
        markerOptions.title(item.getTitle());

        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    private Boolean isInBounds(LatLng position, LatLngBounds latLngBounds) {
        return (latLngBounds == null ? mMap.getProjection().getVisibleRegion().latLngBounds : latLngBounds).contains(position);
    }

}