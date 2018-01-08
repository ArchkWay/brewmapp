package com.brewmapp.data.entity;

import java.text.SimpleDateFormat;

/**
 * Created by Kras on 08.01.2018.
 */

public class PhotoDetails {



    private String id;
    private String related_model;
    private String related_id;
    private Object title;
    private String image;
    private String by_user_id;
    private String front_photo;
    private Object photo_album_id;
    private String timestamp;
    private String user_getThumb;
    private String like;
    private String dis_like;
    private String interested;
    private String no_interested;
    private User_info user_info;
    private Thumb getThumb;

    public User_info getUser_info() {
        return user_info;
    }

    public void setUser_info(User_info user_info) {
        this.user_info = user_info;
    }

    public Thumb getThumb() {
        return getThumb;
    }

    public void setThumb(Thumb thumb) {
        this.getThumb = thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelated_model() {
        return related_model;
    }

    public void setRelated_model(String related_model) {
        this.related_model = related_model;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBy_user_id() {
        return by_user_id;
    }

    public void setBy_user_id(String by_user_id) {
        this.by_user_id = by_user_id;
    }

    public String getFront_photo() {
        return front_photo;
    }

    public void setFront_photo(String front_photo) {
        this.front_photo = front_photo;
    }

    public Object getPhoto_album_id() {
        return photo_album_id;
    }

    public void setPhoto_album_id(Object photo_album_id) {
        this.photo_album_id = photo_album_id;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public String getTimestampFormated() {
        try {

            return new StringBuilder()
                    .append(android.text.format.DateFormat.format("dd.MM.yyyy",new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(getTimestamp())).toString())
                    //.append(" ")
                    //.append(getTimeFrom())
                    .toString();

        }catch (Exception e){
            return null;
        }
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_getThumb() {
        return user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDis_like() {
        return dis_like;
    }

    public void setDis_like(String dis_like) {
        this.dis_like = dis_like;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getNo_interested() {
        return no_interested;
    }

    public void setNo_interested(String no_interested) {
        this.no_interested = no_interested;
    }
}


