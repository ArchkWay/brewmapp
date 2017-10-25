package com.brewmapp.data.pojo;

/**
 * Created by Kras on 21.10.2017.
 */

public class FindBeerPackage {
    private String interestFilter;

    private int mode;
    private int filter;
    private int page;
    private String stringSearch;

    public String getStringSearch() {
        return stringSearch;
    }

    public void setStringSearch(String stringSearch) {
        this.stringSearch = stringSearch;
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
