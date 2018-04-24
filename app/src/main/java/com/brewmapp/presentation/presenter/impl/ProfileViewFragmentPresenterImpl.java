package com.brewmapp.presentation.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.execution.task.ListFriendsTask;
import com.brewmapp.execution.task.LoadFreeProfileTask;
import com.brewmapp.presentation.presenter.contract.ProfileViewFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public class ProfileViewFragmentPresenterImpl extends BasePresenter<ProfileViewFragmentView> implements ProfileViewFragmentPresenter {

    private User user_old_data;

    private LoadFreeProfileTask loadFreeProfileTask;
    private ListFriendsTask listFriendsTask;
    private UserRepo userRepo;

    @Inject
    public ProfileViewFragmentPresenterImpl( LoadFreeProfileTask loadFreeProfileTask,ListFriendsTask listFriendsTask,UserRepo userRepo){

        this.loadFreeProfileTask = loadFreeProfileTask;
        this.listFriendsTask = listFriendsTask;
        this.userRepo = userRepo;

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

            listFriendsTask.execute(String.valueOf(user_id),new SimpleSubscriber<List<IFlexible>>(){
                @Override
                public void onNext(List<IFlexible> iFlexibles) {
                    super.onNext(iFlexibles);
                    int owner_id=userRepo.load().getId();
                    Iterator<IFlexible> iterator=iFlexibles.iterator();
                    while (iterator.hasNext()){
                        IFlexible iFlexible=iterator.next();
                        if(iFlexible instanceof ContactInfo) {
                            ContactInfo contactInfo=(ContactInfo) iFlexible;
                            User user = contactInfo.getModel().getUser();
                            if (user.getId() == owner_id) {
                                view.setStatus(contactInfo.getModel().getStatus());
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    view.commonError(e.getMessage());
                }
            });



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
