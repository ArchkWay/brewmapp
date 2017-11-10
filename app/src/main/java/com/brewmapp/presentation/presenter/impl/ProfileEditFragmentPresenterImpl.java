package com.brewmapp.presentation.presenter.impl;

import android.net.Uri;
import android.widget.RadioGroup;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.pojo.ProfileChangePackage;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.ProfileChangeTask;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileInfoActivity;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public class ProfileEditFragmentPresenterImpl extends BasePresenter<ProfileEditFragmentView> implements ProfileEditFragmentPresenter {

    private User user;
    private ProfileChangeTask profileChangeTask;

    @Inject
    public ProfileEditFragmentPresenterImpl(UserRepo userRepo, ProfileChangeTask profileChangeTask){
        user=userRepo.load();
        this.profileChangeTask = profileChangeTask;
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
    public void save(ProfileChangePackage profileChangePackage, ProfileEditFragment.OnFragmentInteractionListener mListener) {
        profileChangeTask.execute(profileChangePackage,new SimpleSubscriber<ListResponse<User>>(){
            @Override
            public void onNext(ListResponse<User> userListResponse) {
                super.onNext(userListResponse);
                mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileInfoActivity.FRAGMENT_INFO)));
                mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileInfoActivity.FRAGMENT_USER_SAVED)));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }
}
