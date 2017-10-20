package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.FavoriteRestoPresenter;
import com.brewmapp.presentation.view.contract.FavoriteRestoView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 20.10.2017.
 */

public class FavoriteRestoPresenterImpl extends BasePresenter<FavoriteRestoView> implements FavoriteRestoPresenter{

    @Inject
    public FavoriteRestoPresenterImpl(){

    }

    @Override
    public void onAttach(FavoriteRestoView favoriteRestoView) {
        super.onAttach(favoriteRestoView);
    }

    @Override
    public void onDestroy() {

    }
}
