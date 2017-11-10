package com.brewmapp.presentation.presenter.impl;

import android.widget.RadioGroup;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.task.ProfileChangeTask;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public class ProfileEditFragmentPresenterImpl extends BasePresenter<ProfileEditFragmentView> implements ProfileEditFragmentPresenter {

    private User user;
    private ProfileChangeTask profileChangeTask;

    @Inject
    public ProfileEditFragmentPresenterImpl(UserRepo userRepo){
        user=userRepo.load();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(ProfileEditFragmentView profileEditFragmentView) {
        super.onAttach(profileEditFragmentView);
        view.refreshProfile(user);
    }

    @Override
    public CharSequence getTitle() {
        return user.getFormattedName();
    }

    @Override
    public RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                user.setGender(checkedId== R.id.fragment_profile_edit_man?1:0);
            }
        };
    }

    @Override
    public void save() {

    }
}
