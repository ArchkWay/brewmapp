package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.presentation.view.contract.PickLocationView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.PickLocationPresenter;

public class PickLocationPresenterImpl extends BasePresenter<PickLocationView> implements PickLocationPresenter {

    @Inject
    public PickLocationPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
