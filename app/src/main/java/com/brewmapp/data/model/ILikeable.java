package com.brewmapp.data.model;

/**
 * Created by oleg on 31.08.17.
 */

public interface ILikeable {
    void increaseLikes();

    void increaseDisLikes();

    int getLike();
}
