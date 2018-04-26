package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.fragment.FriendsFragment;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface FriendsPresenter extends LivePresenter<FriendsView> {
    void loadFriends(boolean subscribers);

    void requestNewFriend(Intent data);

    void setItemTouchHelper(RecyclerView list);

    void onClickItem(int code, Object payload, BaseActivity baseActivity);

    void searchFriends(FullSearchPackage fullSearchPackage);

    void deleteFriend(WrapperParams wrapperParams);

    void addFriend(WrapperParams wrapperParams);

    void allowFriend(WrapperParams wrapperParams);
}
