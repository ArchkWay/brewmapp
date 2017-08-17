package ru.frosteye.beermap.execution.social;

/**
 * Created by oleg che on 21.03.16.
 */
public interface SocialAuthResult {
    void onAuthSuccess(SocialCredentials credentials);
}
