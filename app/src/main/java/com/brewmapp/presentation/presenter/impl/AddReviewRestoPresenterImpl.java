package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.AddReviewRestoPresenter;
import com.brewmapp.presentation.view.contract.AddReviewRestoView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 03.11.2017.
 */

public class AddReviewRestoPresenterImpl extends BasePresenter<AddReviewRestoView> implements AddReviewRestoPresenter {

    private RestoDetail restoDetail;

    @Inject
    public AddReviewRestoPresenterImpl(){

    }


    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(AddReviewRestoView addReviewRestoView) {
        super.onAttach(addReviewRestoView);
    }

    @Override
    public void parseIntent(Intent intent) {
        restoDetail= (RestoDetail) intent.getSerializableExtra(Keys.RESTO_ID);
        if(restoDetail==null)
            view.commonError();
        else
            view.setModel(restoDetail);
    }
}
