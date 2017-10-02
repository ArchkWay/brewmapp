package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.presentation.view.contract.SaleDetailsView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;

public class SaleDetailsPresenterImpl extends BasePresenter<SaleDetailsView> implements SaleDetailsPresenter {

    @Inject
    public SaleDetailsPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
