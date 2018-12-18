package com.brewmapp.data.pojo;

/**
 * Created by Kras on 04.11.2017.
 */

public class ReviewPackage extends BasePackage{
    private String related_model;
    private String related_id;
    private String text;
    private String user_id;
    private String city_id;
    private String country_id;
    private int mode, filter, page;
    private int type;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getCity_id() {

        return city_id;
    }
    public void setCity_id(String city_id) {

        this.city_id = city_id;
    }
    public String getCountry_id() {

        return country_id;
    }
    public void setCountry_id(String country_id) {

        this.country_id = country_id;
    }
    public int getFilter() {

        return filter;
    }
    public void setFilter(int filter) {

        this.filter = filter;
    }

    public int getMode() {

        return mode;
    }
    public void setMode(int mode) {

        this.mode = mode;
    }
    public int getPage() {

        return page;
    }
    public void setPage(int page) {

        this.page = page;
    }
    public void dropAll() {
        //dropDates();
        mode = 0;
        filter = 0;
        page = 0;
    }
}
