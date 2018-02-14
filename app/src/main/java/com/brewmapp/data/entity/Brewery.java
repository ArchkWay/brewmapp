package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;

public class Brewery implements java.io.Serializable {
    //private static final long serialVersionUID = -6902596022653865455L;
    private String country;
    private String vacancies_count;
    private String short_text;
    private String event_count;
    private String news_count;
    private String location_id;
    private String shares_count;
    private String avg_ball;
    private String getThumb;
    private String name_ru;
    private User user_info;
    private String in_archive;
    private String id;
    private String text;
    private String timestamp;
    private String user_getThumb;
    private String image;
    private String no_interested;
    private String like;
    private String dis_like;
    private String by_user_id;
    private String additional_data;
    private String name;
    private Location location;
    private String interested;
    private String beer_count;
    private String employees_count;
    private boolean selected;
    private boolean selectable;

    public String getGetThumbFormated() {
        if(getThumb != null && !getThumb.startsWith("http")) {
            getThumb = BuildConfig.SERVER_ROOT_URL + getThumb;
        }
        return getThumb;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVacancies_count() {
        return this.vacancies_count;
    }

    public void setVacancies_count(String vacancies_count) {
        this.vacancies_count = vacancies_count;
    }

    public Object getShort_text() {
        return this.short_text;
    }

    public void setShort_text(String short_text) {
        this.short_text = short_text;
    }

    public String getEvent_count() {
        return this.event_count;
    }

    public void setEvent_count(String event_count) {
        this.event_count = event_count;
    }

    public String getNews_count() {
        return this.news_count;
    }

    public void setNews_count(String news_count) {
        this.news_count = news_count;
    }

    public String getLocation_id() {
        return this.location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getShares_count() {
        return this.shares_count;
    }

    public void setShares_count(String shares_count) {
        this.shares_count = shares_count;
    }

    public Object getAvg_ball() {
        return this.avg_ball;
    }

    public void setAvg_ball(String avg_ball) {
        this.avg_ball = avg_ball;
    }

    public Object getGetThumb() {
        return this.getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public String getName_ru() {
        return this.name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }

    public User getUser_info() {
        return this.user_info;
    }

    public void setUser_info(User user_info) {
        this.user_info = user_info;
    }

    public String getIn_archive() {
        return this.in_archive;
    }

    public void setIn_archive(String in_archive) {
        this.in_archive = in_archive;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_getThumb() {
        return this.user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNo_interested() {
        return this.no_interested;
    }

    public void setNo_interested(String no_interested) {
        this.no_interested = no_interested;
    }

    public String getLike() {
        return this.like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDis_like() {
        return this.dis_like;
    }

    public void setDis_like(String dis_like) {
        this.dis_like = dis_like;
    }

    public String getBy_user_id() {
        return this.by_user_id;
    }

    public void setBy_user_id(String by_user_id) {
        this.by_user_id = by_user_id;
    }

    public String getAdditional_data() {
        return this.additional_data;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
    }

    public String getName() {
        return this.name;
    }

    public String getFormatedTitle() {
        String titleFormated=null;
        if(getName()!=null)
            titleFormated=getName();
        if(getName_ru()!=null)
            titleFormated=new StringBuilder().append(titleFormated).append(" (").append(getName_ru()).append(")").toString();

        return titleFormated==null?"":titleFormated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getInterested() {
        return this.interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getBeer_count() {
        return this.beer_count;
    }

    public void setBeer_count(String beer_count) {
        this.beer_count = beer_count;
    }

    public String getEmployees_count() {
        return this.employees_count;
    }

    public void setEmployees_count(String employees_count) {
        this.employees_count = employees_count;
    }
}
