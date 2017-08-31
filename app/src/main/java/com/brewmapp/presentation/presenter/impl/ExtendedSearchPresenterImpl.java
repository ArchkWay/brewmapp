package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.presentation.view.contract.ExtendedSearchActivityView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.ExtendedSearchPresenter;

public class ExtendedSearchPresenterImpl extends BasePresenter<ExtendedSearchActivityView> implements ExtendedSearchPresenter {

    @Inject
    public ExtendedSearchPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
