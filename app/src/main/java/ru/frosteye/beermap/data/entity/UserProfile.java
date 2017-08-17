package ru.frosteye.beermap.data.entity;

/**
 * Created by oleg on 16.08.17.
 */

public class UserProfile {
    private User user;

    public UserProfile(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
