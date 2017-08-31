package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.presentation.view.contract.SearchView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.SearchPresenter;

public class SearchPresenterImpl extends BasePresenter<SearchView> implements SearchPresenter {

    @Inject
    public SearchPresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }
}
