package com.brewmapp.data.pojo;

/**
 * Created by Kras on 21.10.2017.
 */

public class LoadProductPackage {
    private String interestFilter;

    private int mode;
    private int filter;
    private int page;
    private String mTitle;
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getInterestFilter() {
        return interestFilter;
    }

    public void setInterestFilter(String interestFilter) {
        this.interestFilter = interestFilter;
    }

}
