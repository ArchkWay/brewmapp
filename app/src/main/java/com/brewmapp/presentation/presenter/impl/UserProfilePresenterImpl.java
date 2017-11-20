package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.LoadProfileTask;
import com.brewmapp.execution.task.containers.contract.ContainerTasks;
import com.brewmapp.presentation.presenter.contract.UserProfilePresenter;
import com.brewmapp.presentation.view.contract.UserProfileView;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import static com.brewmapp.app.environment.RequestCodes.MODE_LOAD_ALL;

/**
 * Created by xpusher on 11/20/2017.
 */

public class UserProfilePresenterImpl extends BasePresenter<UserProfileView> implements UserProfilePresenter {

    private User user;
    private LoadProfileTask loadProfileTask;

    @Inject    public UserProfilePresenterImpl(LoadProfileTask loadProfileTask){
        this.loadProfileTask = loadProfileTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void parseIntent(Intent intent) {
        try {
            String id=((Interest) intent.getSerializableExtra(Keys.CAP_INTEREST)).getUser_info().getId();
            if(id==null || id.length()==0) {view.commonError();return;}
            user=new User();
            user.setId(Integer.parseInt(id));
        } catch (Exception e){
            view.commonError(e.getMessage());return;
        }

        refreshContent(MODE_LOAD_ALL);

    }

    private void refreshContent(int modeLoadAll) {
        loadProfileTask.execute(null, new SimpleSubscriber<UserProfile>() {
            @Override
            public void onNext(UserProfile userProfile) {
                super.onNext(userProfile);
                user=(userProfile.getUser());
                view.refreshContent(user);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });

    }
}
