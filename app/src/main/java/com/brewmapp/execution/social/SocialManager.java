package com.brewmapp.execution.social;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.brewmapp.execution.social.network.FBSocialNetwork;
import com.brewmapp.execution.social.network.PHSocialNetwork;
import com.brewmapp.execution.social.network.TWSocialNetwork;
import com.brewmapp.execution.social.network.VKSocialNetwork;

/**
 * Created by oleg che on 19.03.16.
 */
public class SocialManager {
    public static final String SOCIAL_PREFS = "social_prefs";

    private final Context appContext;
    private final SharedPreferences preferences;

    private Map<Integer, SocialNetwork> networkMap;


    public SocialManager(Context appContext) {
        this.appContext = appContext.getApplicationContext();
        this.preferences = appContext.getSharedPreferences(SOCIAL_PREFS, Context.MODE_PRIVATE);
        init();
    }

    private void init() {
        this.networkMap = new ConcurrentHashMap<>();
        this.networkMap.put(SocialNetwork.VK, new VKSocialNetwork(appContext));
        this.networkMap.put(SocialNetwork.FB, new FBSocialNetwork(appContext));
        this.networkMap.put(SocialNetwork.PH, new PHSocialNetwork(appContext));
        this.networkMap.put(SocialNetwork.TW, new TWSocialNetwork(appContext));
        ((PHSocialNetwork) getSocialNetwork(SocialNetwork.PH)).setAppContext(appContext);
    }

    public SocialNetwork getSocialNetwork(int id) {
        return this.networkMap.get(id);
    }

    public Collection<SocialNetwork> getAllNetworks() {
        return networkMap.values();
    }

    public void getFriends(int network, SocialContactsResult socialContactsResult, SocialErrorListener errorListener) {
        this.networkMap.get(network).getFriends(socialContactsResult, errorListener);
    }

    public void getProfile(int network, SocialProfileResult profileCallback, SocialErrorListener errorListener) {
        this.networkMap.get(network).getProfile(profileCallback, errorListener);
    }

    public void drop() {
        init();
    }
}
