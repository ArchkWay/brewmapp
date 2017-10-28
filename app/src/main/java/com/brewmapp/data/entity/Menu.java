package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;

import java.io.Serializable;

/**
 * Created by Kras on 28.10.2017.
 */

public class Menu implements Serializable {
    private String id;
    private String resto_id;
    private String resto_menu_category_id;
    private String beer_id;
    private String price;
    private String old_price;
    private String stock;
    private String resto_menu_packing_id;
    private String resto_menu_capacity_id;
    private String status;
    private String timestamp;
    private String user_id;
    private String getThumb;
    private User_info user_info;
    private String user_getThumb;
    private String like;
    private String dis_like;
    private String interested;
    private String no_interested;
    private String beer_name;
    private String beer_short_text;
    private String beer_avg_ball;
    private String menu_category_name;
    private String getThumb_beer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResto_id() {
        return resto_id;
    }

    public void setResto_id(String resto_id) {
        this.resto_id = resto_id;
    }

    public String getResto_menu_category_id() {
        return resto_menu_category_id;
    }

    public void setResto_menu_category_id(String resto_menu_category_id) {
        this.resto_menu_category_id = resto_menu_category_id;
    }

    public String getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(String beer_id) {
        this.beer_id = beer_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getResto_menu_packing_id() {
        return resto_menu_packing_id;
    }

    public void setResto_menu_packing_id(String resto_menu_packing_id) {
        this.resto_menu_packing_id = resto_menu_packing_id;
    }

    public String getResto_menu_capacity_id() {
        return resto_menu_capacity_id;
    }

    public void setResto_menu_capacity_id(String resto_menu_capacity_id) {
        this.resto_menu_capacity_id = resto_menu_capacity_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGetThumb() {
        if(getThumb!= null && !getThumb.startsWith("http")) {
            getThumb= BuildConfig.SERVER_ROOT_URL + getThumb;
        }
        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public User_info getUser_info() {
        return user_info;
    }

    public void setUser_info(User_info user_info) {
        this.user_info = user_info;
    }

    public String getUser_getThumb() {
        return user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDis_like() {
        return dis_like;
    }

    public void setDis_like(String dis_like) {
        this.dis_like = dis_like;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getNo_interested() {
        return no_interested;
    }

    public void setNo_interested(String no_interested) {
        this.no_interested = no_interested;
    }

    public String getBeer_name() {
        return beer_name;
    }

    public void setBeer_name(String beer_name) {
        this.beer_name = beer_name;
    }

    public String getBeer_short_text() {
        return beer_short_text;
    }

    public void setBeer_short_text(String beer_short_text) {
        this.beer_short_text = beer_short_text;
    }

    public String getBeer_avg_ball() {
        return beer_avg_ball;
    }

    public void setBeer_avg_ball(String beer_avg_ball) {
        this.beer_avg_ball = beer_avg_ball;
    }

    public String getMenu_category_name() {
        return menu_category_name;
    }

    public void setMenu_category_name(String menu_category_name) {
        this.menu_category_name = menu_category_name;
    }

    public String getGetThumb_beer() {
        return getThumb_beer;
    }

    public void setGetThumb_beer(String getThumb_beer) {
        this.getThumb_beer = getThumb_beer;
    }
}
