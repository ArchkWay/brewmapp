package com.brewmapp.data.pojo;

/**
 * Created by Kras on 22.10.2017.
 */

public class LoadInterestPackage {
    private int page;

    String filterInterest;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public void setFilterInterest(String interest) {
        this.filterInterest =interest;
    }
    public String getFilterInterest() {
        return filterInterest;
    }

}