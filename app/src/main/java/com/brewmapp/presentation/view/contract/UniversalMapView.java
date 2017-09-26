package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.model.ILocation;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface UniversalMapView extends BasicView {
    void showLocation(ILocation location);
}
