package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

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

    @Inject
    public MessageFragmentPresenterImpl(ListFriendsTask listFriendsTask) {
        this.listFriendsTask = listFriendsTask;
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

    }
}
