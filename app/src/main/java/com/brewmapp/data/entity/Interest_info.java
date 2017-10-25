package com.brewmapp.data.entity;

/**
 * Created by xpusher on 10/23/2017.
 */

public class Interest_info {
    String alias_whell;
    String avg_ball;
    String avg_cost;
    String brand_id;
    String brewery_id;
    String by_user_id;
    String capacity_id;
    String country_id;
    String craft;
    String dis_like;
    String filtered;
    String getThumb;
    String id;
    String image;
    String interested;
    String like;
    String no_interested;
    String pricerange_id;
    String product_density_id;
    String product_ibu;
    String product_ibu_id;
    String short_text;
    String strength_id;
    String text;
    String timestamp;
    String title;
    String title_ru;
    String type_eng_id;
    String type_id;
    String user_getThumb;

    public Interest_info(Beer beer) {
        setTitle(beer.getTitle());
        setGetThumb(beer.getGetThumb());
    }

    public Interest_info() {

    }

    public String getAlias_whell() {
        return alias_whell;
    }

    public void setAlias_whell(String alias_whell) {
        this.alias_whell = alias_whell;
    }

    public String getAvg_ball() {
        return avg_ball;
    }

    public void setAvg_ball(String avg_ball) {
        this.avg_ball = avg_ball;
    }

    public String getAvg_cost() {
        return avg_cost;
    }

    public void setAvg_cost(String avg_cost) {
        this.avg_cost = avg_cost;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrewery_id() {
        return brewery_id;
    }

    public void setBrewery_id(String brewery_id) {
        this.brewery_id = brewery_id;
    }

    public String getBy_user_id() {
        return by_user_id;
    }

    public void setBy_user_id(String by_user_id) {
        this.by_user_id = by_user_id;
    }

    public String getCapacity_id() {
        return capacity_id;
    }

    public void setCapacity_id(String capacity_id) {
        this.capacity_id = capacity_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCraft() {
        return craft;
    }

    public void setCraft(String craft) {
        this.craft = craft;
    }

    public String getDis_like() {
        return dis_like;
    }

    public void setDis_like(String dis_like) {
        this.dis_like = dis_like;
    }

    public String getFiltered() {
        return filtered;
    }

    public void setFiltered(String filtered) {
        this.filtered = filtered;
    }

    public String getGetThumb() {
        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getNo_interested() {
        return no_interested;
    }

    public void setNo_interested(String no_interested) {
        this.no_interested = no_interested;
    }

    public String getPricerange_id() {
        return pricerange_id;
    }

    public void setPricerange_id(String pricerange_id) {
        this.pricerange_id = pricerange_id;
    }

    public String getProduct_density_id() {
        return product_density_id;
    }

    public void setProduct_density_id(String product_density_id) {
        this.product_density_id = product_density_id;
    }

    public String getProduct_ibu() {
        return product_ibu;
    }

    public void setProduct_ibu(String product_ibu) {
        this.product_ibu = product_ibu;
    }

    public String getProduct_ibu_id() {
        return product_ibu_id;
    }

    public void setProduct_ibu_id(String product_ibu_id) {
        this.product_ibu_id = product_ibu_id;
    }

    public String getShort_text() {
        return short_text;
    }

    public void setShort_text(String short_text) {
        this.short_text = short_text;
    }

    public String getStrength_id() {
        return strength_id;
    }

    public void setStrength_id(String strength_id) {
        this.strength_id = strength_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_ru() {
        return title_ru;
    }

    public void setTitle_ru(String title_ru) {
        this.title_ru = title_ru;
    }

    public String getType_eng_id() {
        return type_eng_id;
    }

    public void setType_eng_id(String type_eng_id) {
        this.type_eng_id = type_eng_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getUser_getThumb() {
        return user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
    }
}

