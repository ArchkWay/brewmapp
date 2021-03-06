package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.execution.exchange.request.base.Keys;

import java.io.Serializable;

/**
 * Created by Kras on 22.10.2017.
 */

public class Interest implements Serializable {

    private String id;
    private String created_at;
    private String dis_like;
    private String getThumb;
    private String interested;
    private String like;
    private String no_interested;
    private String related_id;
    private String related_model;
    private String user_getThumb;
    private String user_id;
    private Interest_info interest_info;
    private User_info user_info;



    public Interest() {

    }
    public Interest(Beer beer) {
        setInterest_info(new Interest_info(beer));
        setRelated_model(Keys.CAP_BEER);
        setRelated_id(interest_info.getId());
    }

    public Interest(Resto resto) {
        setInterest_info(new Interest_info(resto));
        setRelated_model(Keys.CAP_RESTO);
    }

    public Interest(Interest interest) {
        setUser_info(interest.getUser_info());
        setRelated_model(Keys.CAP_USER);
    }

    public Interest(Review payload) {
        setUser_info(payload.getUser_info());
        setRelated_model(Keys.CAP_USER);
    }

    public User_info getUser_info() {
        return user_info;
    }

    public void setUser_info(User_info user_info) {
        this.user_info = user_info;
    }

    public Interest_info getInterest_info() {
        return interest_info;
    }

    public void setInterest_info(Interest_info interest_info) {
        this.interest_info = interest_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDis_like() {
        return dis_like;
    }

    public void setDis_like(String dis_like) {
        this.dis_like = dis_like;
    }

    public String getGetThumb() {
        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getNo_interested() {
        return no_interested;
    }

    public void setNo_interested(String no_interested) {
        this.no_interested = no_interested;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public String getRelated_model() {
        return related_model;
    }

    public void setRelated_model(String related_model) {
        this.related_model = related_model;
    }

    public String getUser_getThumb() {
        if(user_getThumb!= null && !user_getThumb.startsWith("http")&& !user_getThumb.startsWith("/"))
            user_getThumb= BuildConfig.SERVER_ROOT_URL + user_getThumb;

        return user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
