package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.SearchAllPresenter;
import com.brewmapp.presentation.view.contract.SearchAllView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchAllPresenterImpl extends BasePresenter<SearchAllView> implements SearchAllPresenter {

    @Inject
    public SearchAllPresenterImpl() {
    }

    @Override
    public void onDestroy() {

    }
}
