package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.FilterByCategoryView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by nixus on 01.11.2017.
 */

public interface FilterByCategoryPresenter extends LivePresenter<FilterByCategoryView> {
    void loadRestoTypes();
    void loadKitchenTypes();
}
