package com.brewmapp.presentation.view.contract;

import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.model.Marker;

public interface InfoWindowMap_view {
    void requestData();
    void setListenerFinishLoadData(Handler.Callback listenerFinishLoadData);

    int getVisibility();

    View getView();

    void setVisibility(int visible);

    void setMarker(Marker marker);

    int getWidth();

    int getHeight();

    ViewTreeObserver getViewTreeObserver();
}
