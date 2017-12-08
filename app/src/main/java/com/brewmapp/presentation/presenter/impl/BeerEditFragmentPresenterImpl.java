package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.BeerEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.BeerEditFragmentView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 12/8/2017.
 */

public class BeerEditFragmentPresenterImpl extends BasePresenter<BeerEditFragmentView> implements BeerEditFragmentPresenter {



    @Inject
    public BeerEditFragmentPresenterImpl(){

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(BeerEditFragmentView beerEditFragmentView) {
        super.onAttach(beerEditFragmentView);
    }

    @Override
    public void requestContent(String id_beer) {

    }
}
