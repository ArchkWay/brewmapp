package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.RestoCardView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 26.10.2017.
 */

public interface RestoCardPresenter extends LivePresenter<RestoCardView> {
    void requestResto(String idResto);
}
