package com.brewmapp.data.pojo;

import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.container.Posts;

/**
 * Created by ovcst on 24.08.2017.
 */

public class ProfileInfoPackage {
    private UserProfile userProfile;
    private Posts posts;

    public ProfileInfoPackage(UserProfile userProfile, Posts posts) {
        this.userProfile = userProfile;
        this.posts = posts;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Posts getPosts() {
        return posts;
    }
}
