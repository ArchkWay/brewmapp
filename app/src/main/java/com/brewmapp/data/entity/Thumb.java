package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kras on 10.12.2017.
 */

public class Thumb {
    private String url;

    @SerializedName(Keys.URL_PREVIEW)
    private String thumbUrl;

    public String getUrl() {
        if(url != null && !url.startsWith("http")) {
            url = BuildConfig.SERVER_ROOT_URL + url;
        }
        return url;
    }

    public String getThumbUrl() {
        if(thumbUrl != null && !thumbUrl.startsWith("http")) {
            thumbUrl = BuildConfig.SERVER_ROOT_URL + url;
        }
        return thumbUrl;
    }

}
