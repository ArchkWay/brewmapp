package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.brewmapp.data.model.IPerson;
import com.brewmapp.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 17.08.17.
 */

public class Contact implements IPerson {


    public Contact(){

    }

    public Contact(User user){
        setFriend_info(user);
        setUser_getThumb(user.getThumbnail());
    }

    private String user_getThumb;

    @SerializedName(Keys.FRIEND_ID)
    private int id;

    private int status;

    @SerializedName(Keys.USER_INFO)
    private User user;

    private User friend_info;

    @Expose(serialize = false)
    private ChatDialog chatDialog;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend_info() {
        return friend_info;
    }

    public void setFriend_info(User friend_info) {
        this.friend_info = friend_info;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getTitle() {
        return getUser().getFormattedName();
    }

    @Override
    public String getImageUrl() {
        return getUser().getThumbnail();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ChatDialog getChatDialog() {
        return chatDialog;
    }

    public void setChatDialog(ChatDialog chatDialog) {
        this.chatDialog = chatDialog;
    }

    public String getUser_getThumb() {
        return user_getThumb;
    }
    public String getUser_getThumb_Formatted() {

        if(user_getThumb!= null && !user_getThumb.startsWith("http")&& !user_getThumb.startsWith("/"))
            user_getThumb= BuildConfig.SERVER_ROOT_URL + user_getThumb;

        return user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
    }


}
