
package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;


//     "user_getThumb": {
//        "url": "uploads\/user\/264.original.jpg?30.08.2018",
//        "url_preview": "https:\/\/developer.brewmapp.com\/uploads\/user\/264.w150.h150.jpg?30.08.2018",
//        "front_photo": "",
//        "size": {
    //        "0": 1280,
    //        "1": 1024,
    //        "2": 2,
    //        "3": "width=\"1280\" height=\"1024\"",
    //        "bits": 8,
    //        "channels": 3,
    //        "mime": "image\/jpeg"
//        },
//        "like": "0",
//        "dis_like": "0"
//     }

public class UserGetThumb {

    @SerializedName("url")
    private String mUrl;
    @SerializedName("url_preview")
    private String mUrl_preview;
    @SerializedName("front_photo")
    private String mFront_photo;

    @SerializedName("size")
    private SizeImage mSize;

    @SerializedName("like")
    private String mLike;
    @SerializedName("dis_like")
    private String mDis_like;


    public String getUrl() {
        if(mUrl!= null && !mUrl.startsWith("http")&& !mUrl.startsWith("/"))
            mUrl= BuildConfig.SERVER_ROOT_URL + mUrl;
        return mUrl;
    }
    public void setUrl(String mUrl) {

        this.mUrl = mUrl;
    }
    public String getUrl_preview() {

        return mUrl_preview;
    }
    public void setUrl_preview(String mUrl_preview) {

        this.mUrl_preview = mUrl_preview;
    }
    public String getFront_photo() {

        return mFront_photo;
    }
    public void setFront_photo(String mFront_photo) {

        this.mFront_photo = mFront_photo;
    }
    public String getLike() {

        return mLike;
    }
    public void setLike(String mLike) {

        this.mLike = mLike;
    }
    public String getDis_like() {

        return mDis_like;
    }
    public void setDis_like(String mDis_like) {

        this.mDis_like = mDis_like;
    }

    public SizeImage getSize() {

        return mSize;
    }
    public void setSize(SizeImage mSize) {

        this.mSize = mSize;
    }
}
