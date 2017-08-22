package com.brewmapp.execution.social.network;

import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import com.brewmapp.R;
import com.brewmapp.data.entity.SocialContact;
import com.brewmapp.data.entity.container.Contacts;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.social.CustomTwitterClient;
import com.brewmapp.execution.social.SocialAuthResult;
import com.brewmapp.execution.social.SocialContactsResult;
import com.brewmapp.execution.social.SocialCredentials;
import com.brewmapp.execution.social.SocialErrorListener;
import com.brewmapp.execution.social.SocialException;
import com.brewmapp.execution.social.SocialListener;
import com.brewmapp.execution.social.SocialNetwork;
import com.brewmapp.execution.social.SocialProfileResult;
import com.brewmapp.execution.social.SocialResult;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

/**
 * Created by oleg che on 19.03.16.
 */
public class TWSocialNetwork extends SocialNetwork {

    public static final String USERS_URL = "https://api.twitter.com/1.1/followers/list.json";

    private TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
    private CustomTwitterClient apiClient;
    private TwitterSession sessionData;


    public TWSocialNetwork(Context appContext) {
        super(appContext);
    }

    @Override
    public int getId() {
        return TW;
    }

    @Override
    public void getFriends(SocialContactsResult socialContactsResult, SocialErrorListener errorListener) {
        setSocialContactsResult(socialContactsResult);
        setErrorListener(errorListener);
        apiClient = new CustomTwitterClient(sessionData);
        apiClient.getFollowers().listTwitterFriends(
                getCredentials().getUid(), null, true, true, 100).enqueue(new retrofit2.Callback<CustomTwitterClient.TwitterUsers>() {
            @Override
            public void onResponse(Call<CustomTwitterClient.TwitterUsers> call, Response<CustomTwitterClient.TwitterUsers> response) {
                if(response.isSuccessful()) {
                    CustomTwitterClient.TwitterUsers body = response.body();
                    List<SocialContact> contacts = new ArrayList<SocialContact>();
                    for(CustomTwitterClient.TwitterUser user: body.getUsers()) {
                        SocialContact contact = new SocialContact();
                        contact.setFirstName(user.getName());
                        contact.setPictureUrl(user.getProfileImage());
                        contact.setSocialId(user.getId());
                        contacts.add(contact);
                    }
                    getSocialContactsResult().onFriendsReady(TW, new Contacts(contacts));
                } else {
                    getErrorListener().onSocialError(new SocialException(TW, response.errorBody().toString()));
                }

            }

            @Override
            public void onFailure(Call<CustomTwitterClient.TwitterUsers> call, Throwable t) {
                getErrorListener().onSocialError(new SocialException(TW, t.getMessage()));
            }
        });
    }

    @Override
    public void getProfile(final SocialProfileResult profileCallback, SocialErrorListener errorListener) {


    }

    @Override
    public void notyfy(SocialContact contact, String message, SocialListener<String> listener) {
        apiClient.getFollowers().sendMessage(message, contact.getSocialId()).enqueue(new retrofit2.Callback<CustomTwitterClient.Dummy>() {
            @Override
            public void onResponse(Call<CustomTwitterClient.Dummy> call,
                                   Response<CustomTwitterClient.Dummy> response) {
                if(response.isSuccessful()) {
                    listener.onResult(new SocialResult<String>(true, getString(R.string.message_sent)));
                } else {
                    listener.onResult(new SocialResult<String>(false, getString(R.string.error)));
                }
            }

            @Override
            public void onFailure(Call<CustomTwitterClient.Dummy> call, Throwable t) {
                listener.onResult(new SocialResult<>(false, getString(R.string.error)));
            }
        });
    }

    @Override
    public int getNetworkName() {
        return R.string.tw;
    }

    @Override
    public void auth(BaseActivity activity, SocialAuthResult socialAuthResult, SocialErrorListener errorListener) {
        setSocialAuthResult(socialAuthResult);
        setErrorListener(errorListener);
        twitterAuthClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(final Result<TwitterSession> result) {
                sessionData = result.data;
                SocialCredentials credentials = new SocialCredentials(
                        sessionData.getAuthToken().token, null, String.valueOf(sessionData.getUserId()), TW
                );
                credentials.setToken(sessionData.getAuthToken().token);
                setCredentials(credentials);
                socialAuthResult.onAuthSuccess(credentials);
            }

            @Override
            public void failure(final TwitterException e) {
                errorListener.onSocialError(new SocialException(TW, e.getMessage()));
            }
        });
    }

    @Override
    public boolean isAuthorized() {
        return getCredentials() != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    private static Contacts parseContacts(JSONArray array) {
        ArrayList<SocialContact> results = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                SocialContact contact = new SocialContact();
                JSONObject object = array.getJSONObject(i);
                contact.setSocialId(object.getString(Keys.ID));
                contact.setFirstName(object.getString(Keys.FIRST_NAME));
                contact.setLastName(object.getString(Keys.LAST_NAME));
                contact.setOrigin(SocialNetwork.FB);
                if(object.has(Keys.PICTURE)) {
                    String pic = object.getJSONObject(Keys.PICTURE).getJSONObject(Keys.DATA).getString(Keys.URL);
                    contact.setPictureUrl(pic);
                }
                results.add(contact);
            } catch (Exception e) {
                continue;
            }
        }
        return new Contacts(results);
    }
}
