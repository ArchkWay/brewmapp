package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.BeerDetailView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 30.10.2017.
 */

public interface BeerDetailPresenter extends LivePresenter<BeerDetailView> {
    void requestBeerDetail(String idBeer);
}
