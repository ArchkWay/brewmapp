package com.brewmapp.utils.events.markerCluster;

import android.content.Context;

import com.brewmapp.data.entity.FilterRestoLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by nixus on 02.12.2017.
 */

public class ClusterRender extends DefaultClusterRenderer<FilterRestoLocation> {

    public ClusterRender(Context context, GoogleMap map,
                         ClusterManager<FilterRestoLocation> clusterManager) {
        super(context, map, clusterManager);
    }


    protected void onBeforeClusterItemRendered(FilterRestoLocation item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getIcon());
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}