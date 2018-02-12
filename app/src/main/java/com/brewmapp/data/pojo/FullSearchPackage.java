package com.brewmapp.data.pojo;

/**
 * Created by Kras on 25.10.2017.
 */

public class FullSearchPackage {


    private int page;
    private String stringSearch;

    private String name=null;
    private String type=null;
    private String beer=null;
    private String kitchen=null;
    private String price=null;
    private String city=null;
    private String metro=null;
    private String features=null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeer() {
        return beer;
    }

    public void setBeer(String beer) {
        this.beer = beer;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMetro() {
        return metro;
    }

    public void setMetro(String metro) {
        this.metro = metro;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

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
