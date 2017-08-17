package ru.frosteye.beermap.execution.social;


import ru.frosteye.beermap.data.entity.User;

/**
 * Created by oleg che on 19.03.16.
 */
public interface SocialProfileResult {
    void onProfileReady(User user);
}
