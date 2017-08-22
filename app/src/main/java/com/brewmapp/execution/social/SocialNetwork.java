package com.brewmapp.execution.social;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.brewmapp.data.entity.SocialContact;
import com.brewmapp.data.entity.container.Contacts;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import ru.frosteye.ovsa.marker.Sync;

/**
 * Created by oleg che on 19.03.16.
 */
public abstract class SocialNetwork {
    public static final int JM = 0;
    public static final int PH = 1;
    public static final int VK = 2;
    public static final int FB = 5;
    public static final int OK = 3;
    public static final int ML = 6;
    public static final int TW = 7;
    public static final int GP = 4;

    protected Context appContext;
    private SharedPreferences preferences;

    private SocialAuthResult socialAuthResult;
    private SocialContactsResult socialContactsResult;
    private SocialProfileResult socialProfileResult;
    private SocialErrorListener errorListener;

    public SocialNetwork(Context appContext) {
        this.appContext = appContext;
        this.preferences = appContext.getSharedPreferences(SocialManager.SOCIAL_PREFS, Context.MODE_PRIVATE);
    }

    private Contacts contacts;

    private SocialCredentials credentials;

    public abstract int getId();

    public abstract void getFriends(SocialContactsResult socialContactsResult, SocialErrorListener errorListener);

    public abstract void getProfile(SocialProfileResult profileCallback, SocialErrorListener errorListener);

    public abstract int getNetworkName();

    public abstract void auth(BaseActivity activity, SocialAuthResult socialAuthResult, SocialErrorListener errorListener);

    public SocialAuthResult getSocialAuthResult() {
        return socialAuthResult;
    }

    public void setSocialAuthResult(SocialAuthResult socialAuthResult) {
        this.socialAuthResult = socialAuthResult;
    }

    public SocialContactsResult getSocialContactsResult() {
        return socialContactsResult;
    }

    public void setSocialContactsResult(SocialContactsResult socialContactsResult) {
        this.socialContactsResult = socialContactsResult;
    }

    public SocialProfileResult getSocialProfileResult() {
        return socialProfileResult;
    }

    public void setSocialProfileResult(SocialProfileResult socialProfileResult) {
        this.socialProfileResult = socialProfileResult;
    }

    public SocialErrorListener getErrorListener() {
        return errorListener;
    }

    public void setErrorListener(SocialErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void notyfy(SocialContact contact, String message, SocialListener<String> listener) {
        SocialResult<String> result = new SocialResult<>(false, "not implemented");
        listener.onResult(result);
    }

    @Sync
    public SocialResult<String> notyfy(SocialContact contact, String message) {
        return new SocialResult<>(false, "not implemented");
    }

    public boolean invitationSendAvailable() {
        return false;
    }

    public boolean isAuthorized() {
        new RuntimeException(getNetworkName() + ": auth check not implemented!").printStackTrace();
        return false;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public SocialCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(SocialCredentials credentials) {
        this.credentials = credentials;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {}

}
