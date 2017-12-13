package com.brewmapp.data.entity;

/**
 * Created by xpusher on 12/13/2017.
 */

public class ChatQuery {
    int id;
    String last_msg;

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
