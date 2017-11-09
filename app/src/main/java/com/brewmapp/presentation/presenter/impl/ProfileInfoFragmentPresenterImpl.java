package com.brewmapp.presentation.presenter.impl;


import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.presentation.view.contract.ProfileInfoFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileInfoFragmentView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 11/9/2017.
 */

public class ProfileInfoFragmentPresenterImpl extends BasePresenter<ProfileInfoFragmentView> implements ProfileInfoFragmentPresenter {

    private UserRepo userRepo;

    @Inject
    public ProfileInfoFragmentPresenterImpl(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(ProfileInfoFragmentView profileInfoFragmentView) {
        super.onAttach(profileInfoFragmentView);
        view.refreshProfile(userRepo.load());
    }
}
