package ru.frosteye.beermap.execution.social;

/**
 * Created by oleg che on 19.03.16.
 */
public class SocialKeys {
    public static final String SOCIAL_AUTH_FACEBOOK_KEY = "702243056553229";
    public static final String SOCIAL_AUTH_FACEBOOK_SECRET = "3147e1198ab49d003ffe7981dd4c9da1";
    public static final String[] SOCIAL_AUTH_FACEBOOK_SCOPE = {"public_profile", "email", "user_friends", "user_photos"};


    public static final String SOCIAL_AUTH_GOOGLE_PLUS_KEY = "594372443196-handop3j3a5foho59fphl3l7iqpgnqgs.apps.googleusercontent.com";
    public static final String[] SOCIAL_AUTH_GOOGLE_PLUS_SCOPE = {
            "https://www.googleapis.com/auth/plus.login",
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile"
    };


    public static final String SOCIAL_AUTH_VK_OAUTH2_KEY = "5225489";
    public static final String[] SOCIAL_AUTH_VK_OAUTH2_SCOPE = {"friends", "wall", "email", "phone", "photo_max_orig", "has_mobile", "bdate", "sex"};
    public static final int SOCIAL_AUTH_VK_APP_USER_MODE = 1;
}
