package com.brewmapp.data.entity.wrapper;

import eu.davidea.flexibleadapter.items.IFilterable;
import com.brewmapp.R;

import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.impl.widget.FriendsTitleView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 17.08.17.
 */

public class FriendsTitleInfo extends AdapterItem<String, FriendsTitleView> implements IFilterable {

    private int status;

    public FriendsTitleInfo(String model, int status) {
        super(model);
        this.status=status;
    }

    @Override
    public int getLayoutRes() {
        switch (status){
            case FriendsView.FRIENDS_REQUEST_IN:
            case FriendsView.FRIENDS_REQUEST_OUT:
                return R.layout.view_friends_requests_title;
            default:
                return R.layout.view_friends_title;

        }

    }

    @Override
    public boolean filter(String constraint) {
        return false;
    }

    public int getStatus() {
        return status;
    }
}
