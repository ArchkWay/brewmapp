package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.beermap.execution.task.ListFriendsTask;
import ru.frosteye.beermap.presentation.view.contract.FriendsView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.FriendsPresenter;

public class FriendsPresenterImpl extends BasePresenter<FriendsView> implements FriendsPresenter {

    private ListFriendsTask listFriendsTask;

    @Inject
    public FriendsPresenterImpl(ListFriendsTask listFriendsTask) {
        this.listFriendsTask = listFriendsTask;
    }

    @Override
    public void onDestroy() {
        listFriendsTask.cancel();
    }

    @Override
    public void loadFriends() {
        enableControls(false);
        listFriendsTask.execute(null, new SimpleSubscriber<MessageResponse>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                super.onNext(messageResponse);
            }
        });
    }
}
