package com.brewmapp.presentation.presenter.impl;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.LoadFreeProfileTask;
import com.brewmapp.execution.task.LoadProfileTask;
import com.brewmapp.execution.task.ProfileChangeTask;
import com.brewmapp.execution.task.UploadAvatarTask;
import com.brewmapp.presentation.presenter.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.presenter.contract.ProfileViewFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileViewFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
                    view.refreshProfile(user_old_data);
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

}
