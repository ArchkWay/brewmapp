package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.container.Subscriptions;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 09.11.2017.
 */

public interface ProfileViewFragmentView extends BasicView {
    void setContent(User user);
    void commonError(String... strings);

    void setStatusFriend(int status);

    void friendDeletedSuccess();

    void friendAllowSuccess();

    void requestSendSuccess();

    void setSubscriptions(Subscriptions subscriptions);

    void setNews(Posts posts);

    void subscriptionSuccess(String subscription_id);

    void unSubscriptionSuccess();
}
