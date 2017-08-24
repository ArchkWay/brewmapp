package com.brewmapp.data.pojo;

import java.util.Date;

/**
 * Created by oleg on 23.08.17.
 */

public class LoadNewsPackage {
    private int mode, filter, page;
    private Date dateFrom, dateTo;

    public LoadNewsPackage(int mode, int filter, int page) {
        this.mode = mode;
        this.filter = filter;
        this.page = page;
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
}
