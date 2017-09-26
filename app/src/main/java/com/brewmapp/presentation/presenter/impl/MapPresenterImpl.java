package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.pojo.SimpleLocation;
import com.brewmapp.presentation.view.contract.UniversalMapView;

import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.MapPresenter;

public class MapPresenterImpl extends BasePresenter<UniversalMapView> implements MapPresenter {

    private ActiveBox activeBox;

    @Inject
    public MapPresenterImpl(ActiveBox activeBox) {
        this.activeBox = activeBox;
    }

    @Override
    public void onAttach(UniversalMapView universalMapView) {
        super.onAttach(universalMapView);
        view.showLocation(activeBox.getActive(SimpleLocation.class));
    }

    @Override
    public void onDestroy() {

    }
}
