package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;

import java.util.HashMap;

public class MenuResto {
    /**
     * id : 1826
     * resto_id : 98
     * resto_menu_category_id : null
     * beer_id : 453
     * price : 240
     * old_price : null
     * stock : 0
     * resto_menu_packing_id : 2
     * resto_menu_capacity_id : 1
     * status : 1
     * timestamp : 2016-10-25 18:59:40
     * user_id : null
     * getThumb : null
     * like : 0
     * dis_like : 0
     * interested : 0
     * no_interested : 0
     * beer_name : Abbaye De Floreffe Blonde
     * beer_short_text :
     * beer_avg_ball :
     * menu_category_name : null
     * getThumb_beer : uploads/beer/453.original.jpg?21.07.2017
     */

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
    private String like;
    private String dis_like;
    private String interested;
    private String no_interested;
    private String beer_name;
    private String beer_short_text;
    private String beer_avg_ball;
    private String menu_category_name;
    private String getThumb_beer;
    private HashMap<String,String> capacity_price=new HashMap<>();

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
        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
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

    public String getGetThumbFormated() {
        String getGetThumbFormated=null;
        if(getGetThumb() != null && !getGetThumb().startsWith("http")&& !getGetThumb().startsWith("/")) {
            getGetThumbFormated = BuildConfig.SERVER_ROOT_URL + getGetThumb();
        }
        return getGetThumbFormated;
    }
    public String getGetThumb_beerFormated() {
        String thumb_beerFormated=null;
        if(getGetThumb_beer() != null && !getGetThumb_beer().startsWith("http")&& !getGetThumb_beer().startsWith("/")) {
            thumb_beerFormated = BuildConfig.SERVER_ROOT_URL + getGetThumb_beer();
        }
        return thumb_beerFormated;
    }

    public HashMap<String, String> getCapacity_price() {
        return capacity_price;
    }
}
