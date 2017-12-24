package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.brewmapp.presentation.view.contract.MessageFragmentView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface MessageFragmentPresenter extends LivePresenter<MessageFragmentView> {
    void loadDialogs(boolean subscribers);

    void requestNewFriend(Intent data);

    void setItemTouchHelper(RecyclerView list);
}
