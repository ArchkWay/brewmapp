package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.presentation.view.contract.PhotoSliderView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.PhotoSliderPresenter;

public class PhotoSliderPresenterImpl extends BasePresenter<PhotoSliderView> implements PhotoSliderPresenter {

    @Inject
    public PhotoSliderPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
