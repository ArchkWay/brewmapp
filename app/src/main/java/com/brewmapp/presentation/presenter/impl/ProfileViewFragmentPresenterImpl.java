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
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.data.pojo.SubscriptionPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.AddFriend;
import com.brewmapp.execution.task.AllowFriend;
import com.brewmapp.execution.task.DeleteFriend;
import com.brewmapp.execution.task.ListFriendsTask;
import com.brewmapp.execution.task.LoadFreeProfileTask;
import com.brewmapp.execution.task.LoadNewsTask;
import com.brewmapp.execution.task.LoadSubscriptionsItemsTask;
import com.brewmapp.execution.task.SubscriptionOffTask;
import com.brewmapp.execution.task.SubscriptionOnTask;
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
    private LoadSubscriptionsItemsTask loadSubscriptionsItemsTask;
    private LoadNewsTask loadNewsTask;
    private SubscriptionOnTask subscriptionOnTask;
    private SubscriptionOffTask subscriptionOffTask;

    @Inject
    public ProfileViewFragmentPresenterImpl( LoadFreeProfileTask loadFreeProfileTask,ListFriendsTask listFriendsTask,UserRepo userRepo,Context context,DeleteFriend deleteFriend,AllowFriend allowFriend,AddFriend addFriend,LoadSubscriptionsItemsTask loadSubscriptionsItemsTask,LoadNewsTask loadNewsTask,SubscriptionOnTask subscriptionOnTask,SubscriptionOffTask subscriptionOffTask){

        this.loadFreeProfileTask = loadFreeProfileTask;
        this.listFriendsTask = listFriendsTask;
        this.userRepo = userRepo;
        this.context = context;
        this.deleteFriend = deleteFriend;
        this.allowFriend = allowFriend;
        this.addFriend = addFriend;
        this.loadSubscriptionsItemsTask = loadSubscriptionsItemsTask;
        this.loadNewsTask = loadNewsTask;
        this.subscriptionOnTask = subscriptionOnTask;
        this.subscriptionOffTask = subscriptionOffTask;

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
    public void loadStatusUser() {
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

    @Override
    public void loadSubscription() {
        loadSubscriptionsItemsTask.execute(user_id,new SimpleSubscriber<Subscriptions>(){
            @Override
            public void onNext(Subscriptions subscriptions) {
                super.onNext(subscriptions);
                view.setSubscriptions(subscriptions);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });

    }

    @Override
    public void loadNews() {
        LoadNewsPackage loadNewsPackage=new LoadNewsPackage();
        loadNewsPackage.setUser_id(String.valueOf(user_id));
        loadNewsPackage.setRelated_model(Keys.CAP_USER);
        loadNewsTask.execute(loadNewsPackage,new SimpleSubscriber<Posts>(){
            @Override
            public void onNext(Posts posts) {
                super.onNext(posts);
                view.setNews(posts);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }

    @Override
    public void SubscriptionOnTask() {
        SubscriptionPackage subscriptionPackage=new SubscriptionPackage();
        subscriptionPackage.setRelated_model(Keys.CAP_USER);
        subscriptionPackage.setRelated_id(String.valueOf(user_id));
        subscriptionOnTask.execute(subscriptionPackage,new SimpleSubscriber<String>(){
            @Override
            public void onNext(String s) {
                super.onNext(s);
                view.subscriptionSuccess();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }

    @Override
    public String getUser_id() {
        return String.valueOf(user_id);
    }

    @Override
    public void SubscriptionOffTask(String externalId) {
        SubscriptionPackage subscriptionPackage=new SubscriptionPackage();
        subscriptionPackage.setId(externalId);
        subscriptionOffTask.execute(subscriptionPackage,new SimpleSubscriber<String>(){
            @Override
            public void onNext(String s) {
                super.onNext(s);
                view.unSubscriptionSuccess();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }

}
