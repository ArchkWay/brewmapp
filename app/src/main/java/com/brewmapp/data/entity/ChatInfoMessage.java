package com.brewmapp.data.entity;

import java.util.Date;

/**
 * Created by xpusher on 12/13/2017.
 */

public class ChatInfoMessage {
    String text;
    String date;
    String dir;
    int read;

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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
