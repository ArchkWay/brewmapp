package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.ProfileFragmentFull_presenter;
import com.brewmapp.presentation.view.contract.ProfileFragmentFull_view;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

public class ProfileFragmentFull_presenter_Impl extends BasePresenter<ProfileFragmentFull_view> implements ProfileFragmentFull_presenter {


    @Override
    public void onAttach(ProfileFragmentFull_view profileFragmentFull_view) {
        super.onAttach(profileFragmentFull_view);
        view=profileFragmentFull_view;
    }

    @Inject
    public ProfileFragmentFull_presenter_Impl(){

    }
    @Override
    public void onDestroy() {

    }
}
