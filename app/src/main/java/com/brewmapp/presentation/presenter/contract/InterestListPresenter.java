package com.brewmapp.presentation.presenter.contract;


import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.presentation.view.contract.InterestListView;

import java.io.Serializable;
import java.util.HashMap;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 20.10.2017.
 */

public interface InterestListPresenter extends LivePresenter<InterestListView> {

    void requestInterests(LoadInterestPackage loadInterestPackage);
    void storeInterest(HashMap<Serializable,Serializable> hmAdd, HashMap<Interest,Interest> hmRemove);

    void requestBeer(String id);
}
