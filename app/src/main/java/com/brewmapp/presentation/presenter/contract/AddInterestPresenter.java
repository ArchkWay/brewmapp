package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.FindBeerPackage;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.presentation.view.contract.AddInterestView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 10/20/2017.
 */

public interface AddInterestPresenter extends LivePresenter<AddInterestView> {

    void sendQueryFullSearch(FullSearchPackage fullSearchPackage);
}
