package com.brewmapp.data.pojo;

/**
 * Created by Kras on 22.10.2017.
 */

public class LoadInterestPackage {
    private int page;
    private String related_id;
    private String related_model;
    private String filterInterest;
    private boolean only_curr_user=true;

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

    public boolean isOnly_curr_user() {
        return only_curr_user;
    }

    public void setOnly_curr_user(boolean only_curr_user) {
        this.only_curr_user = only_curr_user;
    }
}
