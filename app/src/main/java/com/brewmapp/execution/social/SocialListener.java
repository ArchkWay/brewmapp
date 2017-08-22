package com.brewmapp.execution.social;

/**
 * Created by oleg on 14.12.16.
 */

public interface SocialListener<T> {
    void onResult(SocialResult<T> result);
}
