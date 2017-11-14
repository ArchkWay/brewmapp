package com.brewmapp.app.environment;

/**
 * Created by oleg on 11.08.17.
 */

public class RequestCodes {
    public static final int REQUEST_INVITE_FRIEND = 111;
    public static final int REQUEST_CREATE_ALBUM = 112;
    public static final int REQUEST_PICK_LOCATION = 113;
    public static final int REQUEST_PICK_POST_SETTINGS = 114;
    public static final int REQUEST_EDIT_ALBUM = 115;
    public static final int REQUEST_INTEREST = 116;
    public static final int REQUEST_CODE_MAP_REFRESH = 3;
    public static final int REQUEST_FILTER_CATEGORY = 122;
    public static final int REQUEST_CODE_REVIEW_RESTO = 117;
    public static final int REQUEST_SHOW_EVENT_FRAGMENT = 118;
    public static final int REQUEST_MAP_FRAGMENT = 119;
    public static final int REQUEST_CODE_REFRESH_ITEMS=120;
    public static final int REQUEST_CODE_REFRESH_STATE=121;
    public static final int REQUEST_CODE_REFRESH_PROFILE=122;


    public static final String ACTION_SHOW_EVENT_FRAGMENT = "show_news_resto";
    public static final String ACTION_MAP_FRAGMENT = "show_map";
    public static final String INTENT_EXTRAS = "extras";

    public final static int ACTION_SELECT =0;
    public final static int ACTION_VIEW =1;

    public  final static int MODE_LOAD_ALL=0;
    public  final static int MODE_LOAD_ONLY_LIKE=1;

}
