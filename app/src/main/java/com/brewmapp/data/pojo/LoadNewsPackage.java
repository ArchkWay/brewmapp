package com.brewmapp.data.pojo;

import java.util.Date;

/**
 * Created by oleg on 23.08.17.
 */

public class LoadNewsPackage {

    private String resto_id;
    private String related_model;
    private int mode, filter, page;
    private Date dateFrom, dateTo;
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRelated_model() {
        return related_model;
    }

    public void setRelated_model(String related_model) {
        this.related_model = related_model;
    }

    public String getResto_id() {
        return resto_id;
    }

    public void setResto_id(String resto_id) {
        this.resto_id = resto_id;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public int getMode() {
        return mode;
    }

    public int getFilter() {
        return filter;
    }

    public int getPage() {
        return page;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setFilter(int filter) {
        this.filter = filter;
        dropDates();
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void dropDates() {
        dateFrom = null;
        dateTo = null;
    }

    public void dropAll() {
        dropDates();
        mode = 0;
        filter = 0;
        page = 0;
    }

}
