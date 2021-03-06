package com.brewmapp.data.pojo;

/**
 * Created by Kras on 22.10.2017.
 */

public class LoadInterestPackage extends BasePackage{
    private int page;
    private String related_id;
    private String related_model;
    private String filterInterest;
    private String user_id;

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public String getRelated_model() {
        return related_model;
    }

    public void setRelated_model(String related_model) {
        this.related_model = related_model;
    }

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
