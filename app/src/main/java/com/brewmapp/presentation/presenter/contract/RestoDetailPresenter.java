package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 26.10.2017.
 */

public interface RestoDetailPresenter extends LivePresenter<RestoDetailView> {

    void changeSubscription();

    void onLoadEverything(String id);

    void startAddReviewRestoActivity(RestoDetailActivity restoDetailActivity);
}
