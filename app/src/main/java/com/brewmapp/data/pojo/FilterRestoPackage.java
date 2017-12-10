package com.brewmapp.data.pojo;

/**
 * Created by nlbochas on 18/11/2017.
 */

public class FilterRestoPackage {

    private String restoCity;
    private String restoTypes;
    private String restoMetroes;
    private String restoKitchens;
    private String restoFeatures;
    private String menuBeer;
    private String restoPrices;
    private String restoAveragepriceRange;
    private String beerTypeID;
    private int resto_discount;
    private String coordStart;
    private String coordEnd;
    private String restoOrderSort;
    private int page;

    public FilterRestoPackage(String restoCity, String restoTypes, String restoMetroes, String restoKitchens,
                              String restoFeatures, String menuBeer, String restoPrices, String restoAveragepriceRange,
                              String beerTypeID, int resto_discount, String coordStart, String coordEnd, String restoOrderSort) {
        this.restoCity = restoCity;
        this.restoTypes = restoTypes;
        this.restoMetroes = restoMetroes;
        this.restoKitchens = restoKitchens;
        this.restoFeatures = restoFeatures;
        this.menuBeer = menuBeer;
        this.restoPrices = restoPrices;
        this.restoAveragepriceRange = restoAveragepriceRange;
        this.beerTypeID = beerTypeID;
        this.resto_discount = resto_discount;
        this.coordStart = coordStart;
        this.coordEnd = coordEnd;
        this.restoOrderSort = restoOrderSort;
    }

    public FilterRestoPackage() {
    }

    public String getRestoCity() {
        return restoCity;
    }

    public void setRestoCity(String restoCity) {
        this.restoCity = restoCity;
    }

    public String getRestoTypes() {
        return restoTypes;
    }

    public void setRestoTypes(String restoTypes) {
        this.restoTypes = restoTypes;
    }

    public String getRestoMetroes() {
        return restoMetroes;
    }

    public void setRestoMetroes(String restoMetroes) {
        this.restoMetroes = restoMetroes;
    }

    public String getRestoKitchens() {
        return restoKitchens;
    }

    public void setRestoKitchens(String restoKitchens) {
        this.restoKitchens = restoKitchens;
    }

    public String getRestoFeatures() {
        return restoFeatures;
    }

    public void setRestoFeatures(String restoFeatures) {
        this.restoFeatures = restoFeatures;
    }

    public String getMenuBeer() {
        return menuBeer;
    }

    public void setMenuBeer(String menuBeer) {
        this.menuBeer = menuBeer;
    }

    public String getRestoPrices() {
        return restoPrices;
    }

    public void setRestoPrices(String restoPrices) {
        this.restoPrices = restoPrices;
    }

    public String getRestoAveragepriceRange() {
        return restoAveragepriceRange;
    }

    public void setRestoAveragepriceRange(String restoAveragepriceRange) {
        this.restoAveragepriceRange = restoAveragepriceRange;
    }

    public String getBeerTypeID() {
        return beerTypeID;
    }

    public void setBeerTypeID(String beerTypeID) {
        this.beerTypeID = beerTypeID;
    }

    public int getResto_discount() {
        return resto_discount;
    }

    public void setResto_discount(int resto_discount) {
        this.resto_discount = resto_discount;
    }

    public String getCoordStart() {
        return coordStart;
    }

    public void setCoordStart(String coordStart) {
        this.coordStart = coordStart;
    }

    public String getCoordEnd() {
        return coordEnd;
    }

    public void setCoordEnd(String coordEnd) {
        this.coordEnd = coordEnd;
    }

    public String getRestoOrderSort() {
        return restoOrderSort;
    }

    public void setRestoOrderSort(String restoOrderSort) {
        this.restoOrderSort = restoOrderSort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
