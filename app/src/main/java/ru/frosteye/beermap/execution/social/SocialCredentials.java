package ru.frosteye.beermap.execution.social;

import java.io.Serializable;

import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.ovsa.execution.network.request.MultipartRequestParams;
import ru.frosteye.ovsa.execution.network.request.Requestable;

/**
 * Created by oleg che on 21.03.16.
 */
public class SocialCredentials implements Requestable, Serializable {
    public static final String KEY = "social_credentials";
    public static final int INNACTIVE = -1;
    private String token, mail, uid;
    private int socialNetwork;

    public SocialCredentials(String token,
                             String mail,
                             String uid,
                             int socialNetwork) {
        this.token = token;
        this.mail = mail;
        this.uid = uid;
        this.socialNetwork = socialNetwork;
    }

    public boolean isFull() {
        return token != null && mail != null && uid != null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(int socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    @Override
    public MultipartRequestParams createMultipartParams() {
        MultipartRequestParams requestParams = new MultipartRequestParams();
        if(mail != null) requestParams.addPart(Keys.EMAIL, mail);
        if(token != null) requestParams.addPart(Keys.TOKEN, token);
        if(uid != null) requestParams.addPart(Keys.UID, uid);
        switch (socialNetwork) {
            case SocialNetwork.VK:
                requestParams.addPart(Keys.PROVIDER, "vk");
                break;
            case SocialNetwork.FB:
                requestParams.addPart(Keys.PROVIDER, "fb");
                break;
            case SocialNetwork.GP:
                requestParams.addPart(Keys.PROVIDER, "gp");
                break;
            case SocialNetwork.ML:
                requestParams.addPart(Keys.PROVIDER, "mr");
                break;
            case SocialNetwork.OK:
                requestParams.addPart(Keys.PROVIDER, "ok");
                break;
        }
        return requestParams;
    }
}
