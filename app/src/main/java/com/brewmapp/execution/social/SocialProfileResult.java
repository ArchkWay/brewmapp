package com.brewmapp.execution.social;


import com.brewmapp.data.entity.User;

/**
 * Created by oleg che on 19.03.16.
 */
public interface SocialProfileResult {
    void onProfileReady(User user);
}
