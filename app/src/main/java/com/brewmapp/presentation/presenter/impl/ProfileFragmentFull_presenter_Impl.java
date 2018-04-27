package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.text.TextUtils;

import com.brewmapp.data.entity.User;
import com.brewmapp.execution.task.LoadUsersTask;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentFull_presenter;
import com.brewmapp.presentation.view.contract.ProfileFragmentFull_view;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

public class ProfileFragmentFull_presenter_Impl extends BasePresenter<ProfileFragmentFull_view> implements ProfileFragmentFull_presenter {

    private LoadUsersTask loadUsersTask;
    private String user_id;


    @Inject
    public ProfileFragmentFull_presenter_Impl(LoadUsersTask loadUsersTask){
        this.loadUsersTask = loadUsersTask;
    }
    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(ProfileFragmentFull_view profileFragmentFull_view) {
        super.onAttach(profileFragmentFull_view);
        view=profileFragmentFull_view;
    }

    @Override
    public boolean parseIntent(Intent intent) {
        user_id=intent.getData().toString();
        if(TextUtils.isEmpty(user_id)){
            return false;
        }else
            return true;
    }

    @Override
    public void loadProfile() {
        loadUsersTask.execute(Integer.valueOf(user_id),new SimpleSubscriber<ArrayList<User>>(){
            @Override
            public void onNext(ArrayList<User> users) {
                super.onNext(users);
                view.showProfile(users);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }
}
