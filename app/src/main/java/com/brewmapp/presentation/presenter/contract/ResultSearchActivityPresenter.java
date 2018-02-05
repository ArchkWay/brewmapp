package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.presentation.view.contract.ResultSearchActivityView;


import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface ResultSearchActivityPresenter extends LivePresenter<ResultSearchActivityView> {
    void loadRestoList(int specialOffer, FullSearchPackage searchPackage);
    void loadBeerList(int craftBeer, int filter, FullSearchPackage searchPackage);
    void loadBrewery(FullSearchPackage searchPackage);
}
