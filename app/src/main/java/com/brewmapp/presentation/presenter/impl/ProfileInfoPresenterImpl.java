package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.presentation.presenter.contract.ProfileInfoPresenter;
import com.brewmapp.presentation.view.contract.ProfileInfoView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 08.11.2017.
 */

public class ProfileInfoPresenterImpl extends BasePresenter<ProfileInfoView> implements ProfileInfoPresenter {

    private UserRepo userRepo;

    @Inject
    public ProfileInfoPresenterImpl(UserRepo userRepo){
        this.userRepo=userRepo;
    }

    @Override
    public void onAttach(ProfileInfoView profileInfoView) {
        super.onAttach(profileInfoView);
        view.refreshUserProfile(userRepo.load());

    }

    @Override
    public void onDestroy() {

    }
}
