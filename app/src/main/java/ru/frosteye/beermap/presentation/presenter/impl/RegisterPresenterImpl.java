package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.presentation.view.contract.RegisterView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.RegisterPresenter;

public class RegisterPresenterImpl extends BasePresenter<RegisterView> implements RegisterPresenter {

    @Inject
    public RegisterPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
