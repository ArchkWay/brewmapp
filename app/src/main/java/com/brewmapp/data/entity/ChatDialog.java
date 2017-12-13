package com.brewmapp.data.entity;

/**
 * Created by xpusher on 12/13/2017.
 */

public class ChatDialog {
    int id;
    int unread;
    User user;
    ChatInfoMessage lastMessage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatInfoMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatInfoMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}
