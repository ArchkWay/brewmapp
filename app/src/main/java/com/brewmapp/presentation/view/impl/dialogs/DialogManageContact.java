package com.brewmapp.presentation.view.impl.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Contact;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.AllowFriend;
import com.brewmapp.execution.task.DeleteFriend;
import com.brewmapp.presentation.presenter.contract.FriendsPresenter;
import com.brewmapp.presentation.presenter.contract.MessageFragmentPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.activity.StartActivity;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.Presenter;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by xpusher on 11/29/2017.
 */

@SuppressLint("ValidFragment")
public class DialogManageContact extends DialogFragment implements DialogInterface.OnClickListener{
    private User userShow;
    private Presenter presenter;

    final int MODE_DELETE_REQUEST_CONTACT=1;
    final int MODE_DELETE_FRIEND_CONTACT =2;
    final int MODE_ACCEPT_REQUEST_CONTACT=3;


    @Inject    DeleteFriend deleteFriend;
    @Inject    AllowFriend allowFriend;

    private int mode;
    @SuppressLint("ValidFragment")
    public DialogManageContact(Context context, FragmentManager supportManagerFragment, Object payload, Presenter presenter) {
        this.presenter=presenter;

        BeerMap.getAppComponent().plus(new PresenterModule((BaseActivity) getActivity())).inject(this);
        try {
            Contact model= (Contact) payload;

            try {
                if(model.getStatus()==0) {
                    userShow=model.getFriend_info();
                }else {
                    userShow=model.getUser();
                }
            }catch (Exception e){return;};

            model= (Contact) payload;
            switch (model.getStatus()){
                case 0:
                    mode=MODE_DELETE_REQUEST_CONTACT;
                    break;
                case 1:
                    //mode= MODE_DELETE_FRIEND_CONTACT;
                    return;
                case 2:
                    mode=MODE_ACCEPT_REQUEST_CONTACT;
                    break;
                default:
                    return;
            }
        }catch (Exception e){return;}
        show(supportManagerFragment,"ssss");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(userShow.getFormattedName());

        switch (mode){
            case MODE_DELETE_REQUEST_CONTACT: {
                    adb
                    .setPositiveButton(R.string.yes, this)
                    .setNeutralButton(R.string.cancel, this)
                    .setMessage(
                            new StringBuilder()
                                    .append("Удалить запрос на дружбу?")
                                    .toString()
                    );
                return adb.create();
            }
            case MODE_DELETE_FRIEND_CONTACT: {
                    adb
                    .setNeutralButton(R.string.cancel, this)
                    .setPositiveButton(R.string.yes, this)
                    .setMessage(
                            new StringBuilder()
                                    .append("Удалить из друзей?")
                                    .toString()
                    );
                return adb.create();
            }
            case MODE_ACCEPT_REQUEST_CONTACT: {
                    adb
                    .setNeutralButton(R.string.cancel, this)
                    .setPositiveButton(R.string.yes, this)
                    .setNegativeButton(R.string.no, this)
                    .setMessage(
                            new StringBuilder()
                                    .append("Да - Добавить в друзья")
                                    .append("\n")
                                    .append("\n")
                                    .append("Нет - Удалить предлоржение дружбы")
                                    .toString()
                            );
                return adb.create();
            }
            default:
                return null;
        }
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
            switch (mode){
                case MODE_DELETE_REQUEST_CONTACT:
                case MODE_DELETE_FRIEND_CONTACT:
                    if(i==BUTTON_POSITIVE){
                        WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
                        wrapperParams.addParam(Keys.USER_ID, userShow.getId());
                        deleteFriend.execute(wrapperParams,new SimpleSubscriber<String>(){
                            @Override
                            public void onNext(String s) {
                                super.onNext(s);
                                refreshParentContent();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                        });
                    }
                    break;
                case MODE_ACCEPT_REQUEST_CONTACT:
                    if(i==BUTTON_POSITIVE){
                        WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
                        wrapperParams.addParam(Keys.USER_ID, userShow.getId());
                        allowFriend.execute(wrapperParams,new SimpleSubscriber<String>(){
                            @Override
                            public void onNext(String s) {
                                super.onNext(s);
                                refreshParentContent();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                        });
                    }else if(i==BUTTON_NEGATIVE){
                        WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
                        wrapperParams.addParam(Keys.USER_ID, userShow.getId());
                        deleteFriend.execute(wrapperParams,new SimpleSubscriber<String>(){
                            @Override
                            public void onNext(String s) {
                                super.onNext(s);
                                refreshParentContent();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                        });
                    }
                    break;
            }
    }

    private void refreshParentContent() {
        if(presenter instanceof MessageFragmentPresenter)
            ((MessageFragmentPresenter)presenter).loadFriends(false);
        else if(presenter instanceof FriendsPresenter)
            ((FriendsPresenter)presenter).loadFriends(false);
    }
}
