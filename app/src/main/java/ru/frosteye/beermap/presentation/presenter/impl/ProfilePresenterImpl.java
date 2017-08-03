package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.presentation.presenter.contract.ProfilePresenter;
import ru.frosteye.beermap.presentation.view.contract.ProfileView;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 03.08.2017.
 */

public class ProfilePresenterImpl extends BasePresenter<ProfileView> implements ProfilePresenter {

    @Inject
    public ProfilePresenterImpl() {
    }

    @Override
    public void onDestroy() {

    }
}
