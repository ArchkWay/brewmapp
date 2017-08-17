package ru.frosteye.beermap.execution.social.network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.SocialContact;
import ru.frosteye.beermap.data.entity.container.Contacts;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.social.SocialAuthResult;
import ru.frosteye.beermap.execution.social.SocialContactsResult;
import ru.frosteye.beermap.execution.social.SocialCredentials;
import ru.frosteye.beermap.execution.social.SocialErrorListener;
import ru.frosteye.beermap.execution.social.SocialException;
import ru.frosteye.beermap.execution.social.SocialKeys;
import ru.frosteye.beermap.execution.social.SocialNetwork;
import ru.frosteye.beermap.execution.social.SocialProfileResult;
import ru.frosteye.beermap.presentation.view.impl.activity.BaseActivity;

/**
 * Created by oleg che on 19.03.16.
 */
public class FBSocialNetwork extends SocialNetwork {
    private CallbackManager callbackManager;
    private AccessToken accessToken;

    public FBSocialNetwork(Context appContext) {
        super(appContext);
        FacebookSdk.sdkInitialize(appContext.getApplicationContext());
        this.callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, loginResultFacebookCallback);
    }

    @Override
    public int getId() {
        return FB;
    }

    @Override
    public void getFriends(SocialContactsResult socialContactsResult, SocialErrorListener errorListener) {
        setSocialContactsResult(socialContactsResult);
        setErrorListener(errorListener);
        GraphRequest request = GraphRequest.newMyFriendsRequest(accessToken, (objects, response) -> {
            try {
                getSocialContactsResult().onFriendsReady(SocialNetwork.FB, parseContacts(objects));
                setSocialContactsResult(null);
            } catch (Exception e) {
                getErrorListener().onSocialError(new SocialException(SocialNetwork.FB, e.getMessage()));
            }

        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,birthday,first_name,last_name,picture,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void getProfile(final SocialProfileResult profileCallback, SocialErrorListener errorListener) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {
                try {
                    User user = new User();
                    user.setEmail(object.getString(Keys.EMAIL));
                } catch (Exception e) {

                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,birthday,picture,email,gender");
            request.setParameters(parameters);
            request.executeAsync();
    }

    @Override
    public int getNetworkName() {
        return R.string.fb;
    }

    @Override
    public void auth(BaseActivity activity, SocialAuthResult socialAuthResult, SocialErrorListener errorListener) {
        setSocialAuthResult(socialAuthResult);
        setErrorListener(errorListener);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken == null || accessToken.isExpired()) {
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(SocialKeys.SOCIAL_AUTH_FACEBOOK_SCOPE));
        } else {
            setAccessToken(accessToken);
            processToken();
        }
    }

    private void processToken() {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {
            try {
                SocialCredentials socialCredentials = new SocialCredentials(
                        accessToken.getToken(), object.getString(Keys.EMAIL), accessToken.getUserId(), SocialNetwork.FB);
                getSocialAuthResult().onAuthSuccess(socialCredentials);
                setSocialAuthResult(null);
            } catch (Exception e) {
                getErrorListener().onSocialError(new SocialException(SocialNetwork.FB, e.getLocalizedMessage()));
                setSocialAuthResult(null);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,birthday,picture,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private FacebookCallback<LoginResult> loginResultFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            setAccessToken(loginResult.getAccessToken());
            processToken();
        }

        @Override
        public void onCancel() {
            getErrorListener().onSocialError(new SocialException(SocialNetwork.FB, "Отменено"));
            setSocialAuthResult(null);
        }

        @Override
        public void onError(FacebookException error) {
            getErrorListener().onSocialError(new SocialException(SocialNetwork.FB, error.getMessage()));
            setSocialAuthResult(null);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
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
