package com.brewmapp.presentation.presenter.impl;

import android.net.Uri;
import android.widget.RadioGroup;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.ProfileChangeTask;
import com.brewmapp.execution.task.UploadAvatarTask;
import com.brewmapp.presentation.presenter.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileInfoActivity;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;

import java.io.File;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public class ProfileEditFragmentPresenterImpl extends BasePresenter<ProfileEditFragmentView> implements ProfileEditFragmentPresenter {

    private User user_old_data;
    private User user_new_data=new User();
    private ProfileChangeTask profileChangeTask;
    private UploadAvatarTask uploadAvatarTask;


    @Inject
    public ProfileEditFragmentPresenterImpl(UserRepo userRepo, ProfileChangeTask profileChangeTask,UploadAvatarTask uploadAvatarTask){
        user_old_data =userRepo.load();
        this.profileChangeTask = profileChangeTask;
        this.uploadAvatarTask = uploadAvatarTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(ProfileEditFragmentView profileEditFragmentView) {
        super.onAttach(profileEditFragmentView);
        view.refreshProfile(user_old_data);
    }

    @Override
    public CharSequence getTitle() {
        return user_old_data.getFormattedName();
    }

    @Override
    public RadioGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                user_old_data.setGender(checkedId== R.id.fragment_profile_edit_man?1:0);
            }
        };
    }

    @Override
    public void save(ProfileEditFragment.OnFragmentInteractionListener mListener) {
    if(checkNewDataUser())
        profileChangeTask.execute(user_new_data,new SimpleSubscriber<ListResponse<User>>(){
            @Override
            public void onNext(ListResponse<User> userListResponse) {
                super.onNext(userListResponse);

                uploadAvatarTask.execute(new File(user_new_data.getThumbnail()),new SimpleSubscriber<String>(){
                    @Override
                    public void onNext(String string) {
                        super.onNext(string);
                        mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileInfoActivity.FRAGMENT_USER_SAVED)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }

    @Override
    public boolean checkNewDataUser() {
        return false;
    }

    @Override
    public void setPhoto(File file) {
        user_new_data.setThumbnail(file.getAbsolutePath());
    }

    @Override
    public User getUserWithNewData() {
        return user_new_data;
    }
}
