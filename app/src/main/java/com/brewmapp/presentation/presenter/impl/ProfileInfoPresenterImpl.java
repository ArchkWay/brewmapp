package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.ProfileInfoPresenter;
import com.brewmapp.presentation.view.contract.ProfileInfoView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 08.11.2017.
 */

public class ProfileInfoPresenterImpl extends BasePresenter<ProfileInfoView> implements ProfileInfoPresenter {

    @Inject
    public ProfileInfoPresenterImpl(){

    }

    @Override
    public void onAttach(ProfileInfoView profileInfoView) {
        super.onAttach(profileInfoView);
    }

    @Override
    public void onDestroy() {

    }
}
