package com.brewmapp.data.pojo;

/**
 * Created by Kras on 25.10.2017.
 */

public class FullSearchPackage {

    private String type="NO_TYPE";
    private int page;
    private String stringSearch;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getStringSearch() {
        return stringSearch;
    }

    public void setStringSearch(String stringSearch) {
        this.stringSearch = stringSearch;
    }
}
