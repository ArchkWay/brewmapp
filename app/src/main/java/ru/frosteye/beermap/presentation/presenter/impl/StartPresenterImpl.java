package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.presentation.view.contract.StartView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.StartPresenter;

public class StartPresenterImpl extends BasePresenter<StartView> implements StartPresenter {

    @Inject
    public StartPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
