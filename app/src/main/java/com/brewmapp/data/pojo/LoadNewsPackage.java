package com.brewmapp.data.pojo;

import java.util.Date;

/**
 * Created by oleg on 23.08.17.
 */

public class LoadNewsPackage {
    private int mode, filter, page;
    private Date dateFrom, dateTo;

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
