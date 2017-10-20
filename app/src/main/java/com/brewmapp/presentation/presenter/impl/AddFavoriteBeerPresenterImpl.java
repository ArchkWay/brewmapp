package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.AddFavoriteBeerPresenter;
import com.brewmapp.presentation.view.contract.AddFavoriteBeerView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 10/20/2017.
 */

public class AddFavoriteBeerPresenterImpl extends BasePresenter<AddFavoriteBeerView> implements AddFavoriteBeerPresenter {

    @Inject
    public AddFavoriteBeerPresenterImpl(){

    }



    @Override
    public void onAttach(AddFavoriteBeerView addFavoriteBeerView) {
        super.onAttach(addFavoriteBeerView);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void sendQuery(String query) {

    }
}
