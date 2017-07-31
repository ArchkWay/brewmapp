package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.presentation.view.contract.ConfirmPhoneView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.ConfirmPhonePresenter;

public class ConfirmPhonePresenterImpl extends BasePresenter<ConfirmPhoneView> implements ConfirmPhonePresenter {

    @Inject
    public ConfirmPhonePresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
