package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.RestoEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.RestoEditFragmentView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 10.12.2017.
 */

public class RestoEditFragmentPresenterImpl extends BasePresenter<RestoEditFragmentView> implements RestoEditFragmentPresenter {

    @Inject    public RestoEditFragmentPresenterImpl(){    }


    @Override
    public void onDestroy() {

    }
}
