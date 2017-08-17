package ru.frosteye.beermap.execution.social;


import ru.frosteye.beermap.data.entity.container.Contacts;

/**
 * Created by oleg che on 19.03.16.
 */
public interface SocialContactsResult {
    void onFriendsReady(int network, Contacts contacts);
}
