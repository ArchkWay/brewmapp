
package com.brewmapp.data.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Relations {
    private BeerBrand BeerBrand;
    private List<BeerAftertaste> BeerAftertaste;
    private List<BeerAveragePrice> BeerAveragePrice;
    private List<BeerAftertaste> BeerColor;
    private List<BeerAftertaste> BeerFragrance;
    private BeerAftertaste BeerStrength;
    private List<BeerAftertaste> BeerTaste;
    private List<BeerToAftertastes> BeerToAftertastes;
    private List<BeerToColors> BeerToColors;
    private List<BeerToFragrances> BeerToFragrances;
    private List<BeerToTastes> BeerToTastes;
    private BeerType BeerType;
    private Brewery Brewery;
    private ProductDensity productDensity;

    @SerializedName("Country")
    private com.brewmapp.data.entity.Country mCountry;
    @SerializedName("Metro")
    private List<com.brewmapp.data.entity.Metro> mMetro;
    @SerializedName("Region")
    private com.brewmapp.data.entity.Region mRegion;

    public ProductDensity getProductDensity() {
        return productDensity;
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
