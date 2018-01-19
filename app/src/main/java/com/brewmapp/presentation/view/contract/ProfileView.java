package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.container.Subscriptions;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 03.08.2017.
 */

public interface ProfileView extends BasicView, RefreshableView {
    void setContent(UserProfile profile);
    void appendPosts(List<IFlexible> posts);
    void onError();
    void appendSubscriptions(Subscriptions subscriptions);
}
