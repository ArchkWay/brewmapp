package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.FriendsView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface FriendsPresenter extends LivePresenter<FriendsView> {
    void loadFriends(boolean subscribers);
}
