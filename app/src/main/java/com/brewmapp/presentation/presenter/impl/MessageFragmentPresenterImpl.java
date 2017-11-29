package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.AddFriend;
import com.brewmapp.execution.task.ListFriendsTask;
import com.brewmapp.presentation.presenter.contract.FriendsPresenter;
import com.brewmapp.presentation.presenter.contract.MessageFragmentPresenter;
import com.brewmapp.presentation.view.contract.MessageFragmentView;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

public class MessageFragmentPresenterImpl extends BasePresenter<MessageFragmentView> implements MessageFragmentPresenter {

    private ListFriendsTask listFriendsTask;
    private AddFriend addFriend;

    @Inject
    public MessageFragmentPresenterImpl(ListFriendsTask listFriendsTask,AddFriend addFriend) {
        this.listFriendsTask = listFriendsTask;
        this.addFriend = addFriend;
    }

    @Override
    public void onDestroy() {
        listFriendsTask.cancel();
    }

    @Override
    public void loadFriends(boolean subscribers) {
        enableControls(false);
        listFriendsTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> result) {
                enableControls(true);
                view.showFriends(result);
            }
        });
    }

    @Override
    public void requestNewFriend(Intent data) {
        try {
            WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
            wrapperParams.addParam(Keys.FRIEND_ID, data.getData().toString());
            addFriend.execute(wrapperParams,new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    loadFriends(true);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    showMessage(e.getMessage());
                }
            });

        }catch (Exception e){showMessage(e.getMessage());}
    }
}
