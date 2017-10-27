package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.RestoDetailView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 26.10.2017.
 */

public interface RestoDetailPresenter extends LivePresenter<RestoDetailView> {
    void requestRestoDetail(String idResto);
}
