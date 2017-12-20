package com.brewmapp.data.entity;

import java.util.List;

/**
 * Created by xpusher on 12/13/2017.
 */

public class ChatListMessages {
    private List<ChatMessage> data;
    private ChatQuery query;
    private int count;
    private User fromUser;
    private int pageCount;

    public List<ChatMessage> getData() {
        return data;
    }

    public void setData(List<ChatMessage> data) {
        this.data = data;
    }

    public ChatQuery getQuery() {
        return query;
    }

    public void setQuery(ChatQuery query) {
        this.query = query;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
