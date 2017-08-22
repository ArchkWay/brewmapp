package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.presentation.view.contract.PickLocationView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.PickLocationPresenter;

public class PickLocationPresenterImpl extends BasePresenter<PickLocationView> implements PickLocationPresenter {

    @Inject
    public PickLocationPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
