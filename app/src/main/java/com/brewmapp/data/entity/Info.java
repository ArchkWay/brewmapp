package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by Kras on 08.01.2018.
 */

public class Info implements Serializable{
    private int id;
    private String title;
    private String timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
