package com.brewmapp.execution.social.network;

import android.content.Context;
import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import java.util.ArrayList;

import com.brewmapp.R;
import com.brewmapp.data.entity.SocialContact;
import com.brewmapp.data.entity.container.Contacts;
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
public class VKSocialNetwork extends SocialNetwork {
    private VKAccessToken accessToken;
    private boolean isAuthorized = false;

    public VKSocialNetwork(Context appContext) {
        super(appContext);
        VKSdk.initialize(appContext);
    }

    @Override
    public int getId() {
        return VK;
    }

    @Override
    public void getFriends(final SocialContactsResult socialContactsResult, final SocialErrorListener errorListener) {
        VKApi.friends()
                .get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,email,photo_100,bdate"))
                .executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKUsersArray array = (VKUsersArray)response.parsedModel;
                        ArrayList<SocialContact> list = new ArrayList<>();
                        for(VKApiUserFull user: array) {
                            SocialContact contact = new SocialContact();
                            contact.setPhone(user.mobile_phone);
                            contact.setFirstName(user.first_name);
                            contact.setLastName(user.last_name);
                            contact.setPictureUrl(user.photo_100);
                            contact.setOrigin(SocialNetwork.VK);
                            contact.setSocialId(String.valueOf(user.id));
                            list.add(contact);
                        }
                        setContacts(new Contacts(list));
                        socialContactsResult.onFriendsReady(SocialNetwork.VK, getContacts());
                    }

                    @Override
                    public void onError(VKError error) {
                        errorListener.onSocialError(new SocialException(SocialNetwork.VK, error.errorMessage));
                    }
                });
    }

    @Override
    public void getProfile(SocialProfileResult profileCallback, SocialErrorListener errorListener) {
    }

    @Override
    public int getNetworkName() {
        return R.string.vk;
    }

    @Override
    public void auth(BaseActivity activity, SocialAuthResult socialAuthResult, SocialErrorListener errorListener) {
        setSocialAuthResult(socialAuthResult);
        setErrorListener(errorListener);
        if(getCredentials() != null) {
            socialAuthResult.onAuthSuccess(getCredentials());
            return;
        }
        if(VKSdk.wakeUpSession(activity)) {
            isAuthorized = true;
            processCredentials(VKAccessToken.currentToken());
        } else {
            VKSdk.login(activity, VKScope.EMAIL, VKScope.FRIENDS, VKScope.MESSAGES, VKScope.OFFLINE);
        }
    }

    @Override
    public boolean invitationSendAvailable() {
        return true;
    }

    public VKAccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(VKAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    private void processCredentials(VKAccessToken res) {
        accessToken = res;
        SocialCredentials socialCredentials = new SocialCredentials(res.accessToken, res.email, res.userId, SocialNetwork.VK);
        setCredentials(socialCredentials);
        getSocialAuthResult().onAuthSuccess(socialCredentials);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                isAuthorized = true;
                processCredentials(res);
            }

            @Override
            public void onError(VKError error) {
                isAuthorized = false;
                SocialException socialException = new SocialException(SocialNetwork.VK, error.errorMessage);
                getErrorListener().onSocialError(socialException);
            }
        });
    }

    @Override
    public void notyfy(SocialContact contact, String message, SocialListener<String> listener) {
        VKRequest request = new VKRequest("messages.send",
                VKParameters.from(VKApiConst.USER_ID, contact.getSocialId(),
                        VKApiConst.MESSAGE, message));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                listener.onResult(new SocialResult<>(true, getString(R.string.message_sent)));
            }

            @Override
            public void onError(VKError error) {
                listener.onResult(new SocialResult<>(false, error.errorMessage));
            }
        });
    }

    @Override
    public boolean isAuthorized() {
        return isAuthorized;
    }
}
