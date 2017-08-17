package ru.frosteye.beermap.presentation.presenter.impl;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
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
}
