package com.brewmapp.presentation.view.contract;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface FriendsView extends BasicView {
    int FRIENDS_REQUEST_OUT = 0;
    int FRIENDS_REQUEST_IN =2;
    int FRIENDS_NOW = 1;
    int FRIENDS_DEFAULT = 3;


    void showFriends(List<IFlexible> list);

}
