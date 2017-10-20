package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.FavoriteBeerPresenter;

import com.brewmapp.presentation.view.contract.FavoriteBeerView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 20.10.2017.
 */

public class FavoriteBeerPresenterImpl extends BasePresenter<FavoriteBeerView> implements FavoriteBeerPresenter{

    @Inject
    public FavoriteBeerPresenterImpl(){}

    @Override
    public void onAttach(FavoriteBeerView favoriteBeerView) {
        super.onAttach(favoriteBeerView);
    }

    @Override
    public void onDestroy() {

    }
}
