package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.presentation.view.contract.MessageFragmentView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface MessageFragmentPresenter extends LivePresenter<MessageFragmentView> {
    void loadFriends(boolean subscribers);

    void requestNewFriend(Intent data);
}
