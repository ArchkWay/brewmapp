package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.data.LocalizedStringsDeserializer;
import com.brewmapp.data.entity.wrapper.UserInfo;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.brewmapp.data.entity.contract.Postable;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;

import ru.frosteye.ovsa.tool.DateTools;

/**
 * Created by oleg on 17.08.17.
 */

public class Post implements Postable, Serializable, ILikeable {

    private int id;
    //FIXME !!!
    @JsonAdapter(LocalizedStringsDeserializer.class)
    private LocalizedStrings text;

    @JsonAdapter(LocalizedStringsDeserializer.class)
    private LocalizedStrings name;

    private UserGetThumb user_getThumb;

    private double lat, lon;
    private int like;
    private int dis_like;
    private Date delayedDate;
    private boolean friendsOnly;
    private String hashTag;
    private List<Integer> photoIds = new ArrayList<>();
    private List<UploadPhotoResponse> filesToUpload = new ArrayList<>();
    private String repost_model;
    private String repost_id;
    private Repost repost;
    private String related_model;

    private String related_id;
    private Related_model_data related_model_data;
    private List<Photo> photo=new ArrayList<>();
    private int type;

    @SerializedName(Keys.DATE_NEWS)
    private Date date;

    @SerializedName(Keys.USER_INFO)
    private User user;


    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

    public UserGetThumb getUser_getThumb() {
//        if(user_getThumb!= null && !user_getThumb.startsWith("http")&& !user_getThumb.startsWith("/"))
//            user_getThumb= BuildConfig.SERVER_ROOT_URL + user_getThumb;
//
//        return user_getThumb;
        return user_getThumb;
    }

    public void setUser_getThumb(UserGetThumb user_getThumb) {
//        this.user_getThumb = user_getThumb;
    }

    public String getRelated_model() {
        return related_model;
    }

    public void setRelated_model(String related_model) {
        this.related_model = related_model;
    }

    public Related_model_data getRelated_model_data() {
        return related_model_data;
    }

    public void setRelated_model_data(Related_model_data related_model_data) {
        this.related_model_data = related_model_data;
    }

    public Repost getRepost() {
        return repost;
    }

    public void setRepost(Repost repost) {
        this.repost = repost;
    }

    public String getRepost_model() {
        return repost_model;
    }

    public void setRepost_model(String repost_model) {
        this.repost_model = repost_model;
    }

    public String getRepost_id() {
        return repost_id;
    }

    public void setRepost_id(String repost_id) {
        this.repost_id = repost_id;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    @Override
    public void increaseLikes() {
        like++;
    }

    @Override
    public void increaseDisLikes() {
        dis_like++;
    }

    public Date getDelayedDate() {
        return delayedDate;
    }

    public boolean isFriendsOnly() {
        return friendsOnly;
    }

    public void setFriendsOnly(boolean friendsOnly) {
        this.friendsOnly = friendsOnly;
    }

    public void setDelayedDate(Date delayedDate) {
        this.delayedDate = delayedDate;
    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }
    public String getDateFormated(){
        try {
            return android.text.format.DateFormat.format("dd.MM.yyyy ",getDate()).toString();
        }catch (Exception e){
            return null;
        }

    }

    public int getId() {
        return id;
    }

    public int getLikes() {
        return like;
    }

    public List<Integer> getPhotoIds() {
        return photoIds;
    }

    public List<UploadPhotoResponse> getFilesToUpload() {
        return filesToUpload;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name != null ? name.toString() : null;
    }

    public void setName(String name) {
//        this.name = name;
    }

    public String getText() {
        return text != null ? text.toString() : null;
    }

    public void setText(String text) {
//        this.text = text;
    }

    public boolean validate() {
//        return name != null && !name.isEmpty()
//                && (text != null && !text.isEmpty() || !getFilesToUpload().isEmpty());
        return true;
    }

//    public boolean isStarted() {
//        return text != null && !text.isEmpty()
//                || name != null && !name.isEmpty()
//                || photoIds.size() > 0;
//    }

    public int getLike() {
        return like;
    }

    public int getDislike() {

        return dis_like;
    }

    @Override
    public WrapperParams createParams() {
        WrapperParams params = new WrapperParams(Wrappers.NEWS);
        params.addParam(Keys.RELATED_MODEL, Keys.CAP_USER);
//        params.addParam(Keys.TEXT, text);
       // params.addParam(Keys.NAME, name);

        if(repost_id!=null&repost_model!=null) {
            params.addParam(Keys.REPOST_ID,repost_id);
            params.addParam(Keys.REPOST_MODEL,repost_model);
        }

        if(lat != 0) {
            params.addParam(Keys.LAT, lat);
            params.addParam(Keys.LAT, lon);
        }
        if(friendsOnly) {
            params.addParam(Keys.ONLY_FRIENDS, 1);
        }
        if(hashTag != null) {
            params.addParam(Keys.HASHTAG, hashTag);
        }
        if(delayedDate != null) {
            params.addParam(Keys.DATE_NEWS_DATE, DateTools.formatDashedDate(delayedDate));
            params.addParam(Keys.DATE_NEWS_TIMES, DateTools.formatTime(delayedDate));
        }
        if(!getPhotoIds().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for(Integer integer: getPhotoIds()) {
                builder.append(",").append(integer);
            }
            params.addParam(Keys.PHOTOS_ID, builder.toString().substring(1));
        }
        return params;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getDate_news() {

        return MapUtils.FormatDate(date);
    }

    public String getDateTime() {

        return MapUtils.FormatTime(date);
    }
}
