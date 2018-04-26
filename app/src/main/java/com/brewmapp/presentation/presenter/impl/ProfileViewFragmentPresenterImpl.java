package com.brewmapp.presentation.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.AddFriend;
import com.brewmapp.execution.task.AllowFriend;
import com.brewmapp.execution.task.DeleteFriend;
import com.brewmapp.execution.task.ListFriendsTask;
import com.brewmapp.execution.task.LoadFreeProfileTask;
import com.brewmapp.presentation.presenter.contract.ProfileViewFragmentPresenter;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;
import com.brewmapp.presentation.view.impl.dialogs.DialogConfirm;

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
    private int user_id;
    private Context context;
    private DeleteFriend deleteFriend;
    private AllowFriend allowFriend;
    private AddFriend addFriend;

    @Inject
    public ProfileViewFragmentPresenterImpl( LoadFreeProfileTask loadFreeProfileTask,ListFriendsTask listFriendsTask,UserRepo userRepo,Context context,DeleteFriend deleteFriend,AllowFriend allowFriend,AddFriend addFriend){

        this.loadFreeProfileTask = loadFreeProfileTask;
        this.listFriendsTask = listFriendsTask;
        this.userRepo = userRepo;
        this.context = context;
        this.deleteFriend = deleteFriend;
        this.allowFriend = allowFriend;
        this.addFriend = addFriend;

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
            user_id=Integer.valueOf(intent.getData().toString());

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
    public void loadFriends() {
        listFriendsTask.execute(null,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);

                Iterator<IFlexible> iterator=iFlexibles.iterator();
                while (iterator.hasNext()){
                    IFlexible iFlexible=iterator.next();
                    if(iFlexible instanceof ContactInfo) {
                        ContactInfo contactInfo=(ContactInfo) iFlexible;
                        User friend = contactInfo.getModel().getFriend_info();
                        if (friend.getId() == user_id) {
                            view.setStatusFriend(contactInfo.getModel().getStatus());
                            return;
                        }
                    }
                }
                view.setStatusFriend(FriendsView.FRIENDS_NOBODY);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });

    }

    @Override
    public void finish(FragmentActivity activity) {
        try {
            activity.setResult(Activity.RESULT_OK,new Intent(null,Uri.parse(String.valueOf(user_old_data.getId()))));
            activity.finish();
        }catch (Exception e){}
    }

    @Override
    public void deleteFriend(FragmentManager fragmentManager, String string) {
        //region Delete Friend
        new DialogConfirm(string, fragmentManager, new DialogConfirm.OnConfirm() {
            @Override
            public void onOk() {
                WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
                wrapperParams.addParam(Keys.USER_ID, user_id);
                deleteFriend.execute(wrapperParams,new SimpleSubscriber<String>(){
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        view.friendDeletedSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showMessage(e.getMessage());
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });
        //endregion

    }

    @Override
    public void allowFriens(FragmentManager fragmentManager, String string) {
        new DialogConfirm(string, fragmentManager, new DialogConfirm.OnConfirm() {
            @Override
            public void onOk() {
                WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
                wrapperParams.addParam(Keys.USER_ID, user_id);
                allowFriend.execute(wrapperParams,new SimpleSubscriber<String>(){
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        view.friendAllowSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showMessage(e.getMessage());
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });

    }

    @Override
    public void sendRequestFriends(FragmentManager fragmentManager, String string) {
        new DialogConfirm(string, fragmentManager, new DialogConfirm.OnConfirm() {
            @Override
            public void onOk() {
                WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
                wrapperParams.addParam(Keys.FRIEND_ID, user_id);
                addFriend.execute(wrapperParams,new SimpleSubscriber<String>(){
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        view.requestSendSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.commonError(e.getMessage());
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });

    }

}
