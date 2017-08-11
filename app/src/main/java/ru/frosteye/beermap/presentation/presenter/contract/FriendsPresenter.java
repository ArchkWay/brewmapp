package ru.frosteye.beermap.presentation.presenter.contract;

import ru.frosteye.beermap.presentation.view.contract.FriendsView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface FriendsPresenter extends LivePresenter<FriendsView> {
    void loadFriends();
}
