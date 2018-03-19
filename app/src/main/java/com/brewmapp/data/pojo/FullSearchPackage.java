package com.brewmapp.data.pojo;

/**
 * Created by Kras on 25.10.2017.
 */

public class FullSearchPackage {


    private int page;
    private String stringSearch="";

    private String name=null;
    private String type=null;
    private String beer=null;
    private String kitchen=null;
    private String price=null;
    private String city=null;
    private String metro=null;
    private String features=null;
    private String country=null;
    private String power;
    private String beerBrand;
    private String pack;
    private String brewery;
    private String density;
    private String fitredBeer;
    private String color;
    private String beerFragrance;
    private String taste;
    private String afterTaste;
    private String id;
    private double lat;
    private double lon;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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

    public void setPower(String power) {
        this.power = power;
    }

    public String getPower() {
        return power;
    }

    public String getBeerBrand() {
        return beerBrand;
    }

    public void setBeerBrand(String beerBrand) {
        this.beerBrand = beerBrand;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getPack() {
        return pack;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getDensity() {
        return density;
    }

    public void setFitredBeer(String fitredBeer) {
        this.fitredBeer = fitredBeer;
    }

    public String getFitredBeer() {
        return fitredBeer;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setBeerFragrance(String beerFragrance) {
        this.beerFragrance = beerFragrance;
    }

    public String getBeerFragrance() {
        return beerFragrance;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getTaste() {
        return taste;
    }

    public void setAfterTaste(String afterTaste) {
        this.afterTaste = afterTaste;
    }

    public String getAfterTaste() {
        return afterTaste;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }
}
