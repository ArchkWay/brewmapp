package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.Beer;
import com.brewmapp.presentation.view.contract.BeerEditFragmentView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 12/8/2017.
 */

public interface BeerEditFragmentPresenter extends LivePresenter<BeerEditFragmentView> {
    void requestContent(String id_beer);

    boolean isNeedSave();

    Beer getBeer_new_data();

    void storeChanges();
}
