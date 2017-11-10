package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.execution.task.LoadProfileTask;
import com.brewmapp.presentation.presenter.contract.ProfileInfoPresenter;
import com.brewmapp.presentation.view.contract.ProfileInfoView;
import com.brewmapp.presentation.view.impl.activity.ProfileInfoActivity;
import com.brewmapp.presentation.view.impl.fragment.ProfileInfoFragment;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 08.11.2017.
 */

public class ProfileInfoPresenterImpl extends BasePresenter<ProfileInfoView> implements ProfileInfoPresenter {

    private UserRepo userRepo;
    private LoadProfileTask loadProfileTask;

    @Inject
    public ProfileInfoPresenterImpl(UserRepo userRepo,LoadProfileTask loadProfileTask){
        this.userRepo=userRepo;
        this.loadProfileTask=loadProfileTask;
    }

    @Override
    public void onAttach(ProfileInfoView profileInfoView) {
        super.onAttach(profileInfoView);
        view.refreshUserProfile(userRepo.load());

        //check online
        loadProfileTask.execute(null,new SimpleSubscriber<UserProfile>(){
            @Override
            public void onNext(UserProfile userProfile) {
                super.onNext(userProfile);
                userRepo.save(userProfile.getUser());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });

        view.showFragment(ProfileInfoActivity.FRAGMENT_INFO);
    }

    @Override
    public void onDestroy() {

    }
}
