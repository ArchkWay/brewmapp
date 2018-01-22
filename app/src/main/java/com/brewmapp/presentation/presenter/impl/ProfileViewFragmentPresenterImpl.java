package com.brewmapp.presentation.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.execution.task.LoadFreeProfileTask;
import com.brewmapp.presentation.presenter.contract.ProfileViewFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public class ProfileViewFragmentPresenterImpl extends BasePresenter<ProfileViewFragmentView> implements ProfileViewFragmentPresenter {

    private User user_old_data;

    private LoadFreeProfileTask loadFreeProfileTask;


    @Inject
    public ProfileViewFragmentPresenterImpl( LoadFreeProfileTask loadFreeProfileTask){

        this.loadFreeProfileTask = loadFreeProfileTask;

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(ProfileViewFragmentView profileViewFragmentView) {
        super.onAttach(profileViewFragmentView);
    }

    @Override
    public CharSequence getTitle() {
        return "";
        //return user_old_data.getFormattedName();
    }

    @Override
    public void loadContent(Intent intent) {
        try {
            int user_id=Integer.valueOf(intent.getData().toString());
            loadFreeProfileTask.execute(user_id,new SimpleSubscriber<UserProfile>(){
                @Override
                public void onNext(UserProfile userProfile) {
                    super.onNext(userProfile);
                    user_old_data=userProfile.getUser();
                    view.setContent(user_old_data);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.commonError(e.getMessage());
                }
            });

        }catch (Exception e){
            view.commonError(e.getMessage());
        }

    }

    @Override
    public void finish(FragmentActivity activity) {
        try {
            activity.setResult(Activity.RESULT_OK,new Intent(null,Uri.parse(String.valueOf(user_old_data.getId()))));
            activity.finish();
        }catch (Exception e){}
    }

}
