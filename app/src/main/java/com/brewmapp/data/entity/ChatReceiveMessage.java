package com.brewmapp.data.entity;

import java.util.List;

/**
 * Created by xpusher on 12/14/2017.
 */

public class ChatReceiveMessage {
    private String id;
    private User from;
    private User to;
    private String text;
    private String date;
    private boolean received;
    private boolean read;
    private String serverTime;
    private String dir;
    private String msg_file;

    public String getMsg_file() {
        return msg_file;
    }

    public void setMsg_file(String msg_file) {
        this.msg_file = msg_file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String in) {
        this.dir = in;
    }
}
