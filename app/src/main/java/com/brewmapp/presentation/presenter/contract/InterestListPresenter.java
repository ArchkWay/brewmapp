package com.brewmapp.presentation.presenter.contract;


import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.presentation.view.contract.InterestListView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 20.10.2017.
 */

public interface InterestListPresenter extends LivePresenter<InterestListView> {
    void sendQuery(LoadInterestPackage loadInterestPackage);
}
