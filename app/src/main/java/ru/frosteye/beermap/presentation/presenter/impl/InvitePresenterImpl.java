package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.presentation.view.contract.InviteView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.InvitePresenter;

public class InvitePresenterImpl extends BasePresenter<InviteView> implements InvitePresenter {

    @Inject
    public InvitePresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
