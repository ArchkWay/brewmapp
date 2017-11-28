
package com.brewmapp.data.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Relations implements Serializable {
    private BeerBrand BeerBrand = new BeerBrand();
    private List<BeerAftertaste> BeerAftertaste=new ArrayList<>();
    private List<BeerAveragePrice> BeerAveragePrice=new ArrayList<>();
    private List<BeerAftertaste> BeerColor=new ArrayList<>();
    private List<BeerAftertaste> BeerFragrance=new ArrayList<>();
    private BeerAftertaste BeerStrength=new BeerAftertaste();
    private List<BeerAftertaste> BeerTaste=new ArrayList<>();
    private List<BeerToAftertastes> BeerToAftertastes=new ArrayList<>();
    private List<BeerToColors> BeerToColors=new ArrayList<>();
    private List<BeerToFragrances> BeerToFragrances=new ArrayList<>();
    private List<BeerToTastes> BeerToTastes=new ArrayList<>();
    private BeerType BeerType=new BeerType();
    private Brewery Brewery=new Brewery();
    private ProductDensity productDensity=new ProductDensity();

    @SerializedName("City")
    private com.brewmapp.data.entity.City mCity;
    @SerializedName("Country")
    private com.brewmapp.data.entity.Country mCountry;
    @SerializedName("Metro")
    private List<com.brewmapp.data.entity.Metro> mMetro;
    @SerializedName("Region")
    private com.brewmapp.data.entity.Region mRegion;

    public ProductDensity getProductDensity() {
        return productDensity;
    }

    public City getmCity() {
        return mCity;
    }

    public void setmCity(City mCity) {
        this.mCity = mCity;
    }

    public Country getmCountry() {
        return mCountry;
    }

    public void setmCountry(Country mCountry) {
        this.mCountry = mCountry;
    }

    public void setProductDensity(ProductDensity productDensity) {
        this.productDensity = productDensity;
    }

    public com.brewmapp.data.entity.Brewery getBrewery() {
        return Brewery;
    }

    public void setBrewery(com.brewmapp.data.entity.Brewery brewery) {
        Brewery = brewery;
    }

    public com.brewmapp.data.entity.BeerType getBeerType() {
        return BeerType;
    }

    public void setBeerType(com.brewmapp.data.entity.BeerType beerType) {
        BeerType = beerType;
    }

    public List<com.brewmapp.data.entity.BeerToTastes> getBeerToTastes() {
        return BeerToTastes;
    }

    public void setBeerToTastes(List<com.brewmapp.data.entity.BeerToTastes> beerToTastes) {
        BeerToTastes = beerToTastes;
    }

    public List<com.brewmapp.data.entity.BeerToFragrances> getBeerToFragrances() {
        return BeerToFragrances;
    }

    public void setBeerToFragrances(List<com.brewmapp.data.entity.BeerToFragrances> beerToFragrances) {
        BeerToFragrances = beerToFragrances;
    }

    public List<com.brewmapp.data.entity.BeerToColors> getBeerToColors() {
        return BeerToColors;
    }

    public void setBeerToColors(List<com.brewmapp.data.entity.BeerToColors> beerToColors) {
        BeerToColors = beerToColors;
    }

    public List<com.brewmapp.data.entity.BeerToAftertastes> getBeerToAftertastes() {
        return BeerToAftertastes;
    }

    public void setBeerToAftertastes(List<com.brewmapp.data.entity.BeerToAftertastes> beerToAftertastes) {
        BeerToAftertastes = beerToAftertastes;
    }

    public List<com.brewmapp.data.entity.BeerAftertaste> getBeerTaste() {
        return BeerTaste;
    }

    public void setBeerTaste(List<com.brewmapp.data.entity.BeerAftertaste> beerTaste) {
        BeerTaste = beerTaste;
    }
    public com.brewmapp.data.entity.BeerAftertaste getBeerStrength() {
        return BeerStrength;
    }

    public void setBeerStrength(com.brewmapp.data.entity.BeerAftertaste beerStrength) {
        BeerStrength = beerStrength;
    }
    public List<com.brewmapp.data.entity.BeerAftertaste> getBeerFragrance() {
        return BeerFragrance;
    }

    public void setBeerFragrance(List<com.brewmapp.data.entity.BeerAftertaste> beerFragrance) {
        BeerFragrance = beerFragrance;
    }

    public List<com.brewmapp.data.entity.BeerAftertaste> getBeerColor() {
        return BeerColor;
    }

    public void setBeerColor(List<com.brewmapp.data.entity.BeerAftertaste> beerColor) {
        BeerColor = beerColor;
    }

    public com.brewmapp.data.entity.Country getCountry() {
        return mCountry;
    }

    public void setCountry(com.brewmapp.data.entity.Country Country) {
        mCountry = Country;
    }

    public List<com.brewmapp.data.entity.Metro> getMetro() {
        return mMetro;
    }

    public void setMetro(List<com.brewmapp.data.entity.Metro> Metro) {
        mMetro = Metro;
    }

    public com.brewmapp.data.entity.Region getRegion() {
        return mRegion;
    }

    public void setRegion(com.brewmapp.data.entity.Region Region) {
        mRegion = Region;
    }

    public List<com.brewmapp.data.entity.BeerAftertaste> getBeerAftertaste() {
        return BeerAftertaste;
    }

    public void setBeerAftertaste(List<com.brewmapp.data.entity.BeerAftertaste> beerAftertaste) {
        BeerAftertaste = beerAftertaste;
    }
    public List<com.brewmapp.data.entity.BeerAveragePrice> getBeerAveragePrice() {
        return BeerAveragePrice;
    }

    public void setBeerAveragePrice(List<com.brewmapp.data.entity.BeerAveragePrice> beerAveragePrice) {
        BeerAveragePrice = beerAveragePrice;
    }

    public com.brewmapp.data.entity.BeerBrand getBeerBrand() {
        return BeerBrand;
    }

    public void setBeerBrand(com.brewmapp.data.entity.BeerBrand beerBrand) {
        BeerBrand = beerBrand;
    }
}
