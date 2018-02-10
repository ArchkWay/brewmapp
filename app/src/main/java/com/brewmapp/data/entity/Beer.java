package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;

import java.io.Serializable;

/**
 * Created by xpusher on 10/20/2017.
 */

public class Beer implements Serializable {

    private String id;
    private String country_id;
    private String brand_id;
    private String type_id;
    private String type_eng_id;
    private String product_density_id;
    private String product_ibu_id;
    private String strength_id;
    private String brewery_id;
    private String title;
    private String title_ru;
    private String text;
    private String short_text;
    private String avg_cost;
    private String by_user_id;
    private String craft;
    private String filtered;
    private String alias_whell;
    private String timestamp;
    private String image;
    private String getThumb;
    private boolean selected;
    private Relations relations=new Relations();
    private String like;
    private String dis_like;
    private boolean selectable;

    public Beer(String id_beer) {
        setId(id_beer);
    }

    public Beer(){

    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
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

    public Relations getRelations() {
        return relations;
    }

    public void setRelations(Relations relations) {
        this.relations = relations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_eng_id() {
        return type_eng_id;
    }

    public void setType_eng_id(String type_eng_id) {
        this.type_eng_id = type_eng_id;
    }

    public String getProduct_density_id() {
        return product_density_id;
    }

    public void setProduct_density_id(String product_density_id) {
        this.product_density_id = product_density_id;
    }

    public String getProduct_ibu_id() {
        return product_ibu_id;
    }

    public void setProduct_ibu_id(String product_ibu_id) {
        this.product_ibu_id = product_ibu_id;
    }

    public String getStrength_id() {
        return strength_id;
    }

    public void setStrength_id(String strength_id) {
        this.strength_id = strength_id;
    }

    public String getBrewery_id() {
        return brewery_id;
    }

    public void setBrewery_id(String brewery_id) {
        this.brewery_id = brewery_id;
    }

    public String getTitle() {
        return title==null?"":title;
    }

    public String getFormatedTitle() {
        String titleFormated=null;
        if(getTitle()!=null)
            titleFormated=getTitle();
        if(getTitle_ru()!=null)
            titleFormated=new StringBuilder().append(titleFormated).append(" (").append(getTitle_ru()).append(")").toString();

        return titleFormated==null?"":titleFormated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_ru() {
        return (title_ru==null||title_ru.length()==0)?getTitle():title_ru;
    }

    public String getTitleRU() {
        return title_ru;
    }

    public void setTitle_ru(String title_ru) {
        this.title_ru = title_ru;
    }

    public String getText() {
        return text==null?"":text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getShort_text() {
        return short_text;
    }

    public void setShort_text(String short_text) {
        this.short_text = short_text;
    }

    public String getAvg_cost() {
        return avg_cost;
    }

    public void setAvg_cost(String avg_cost) {
        this.avg_cost = avg_cost;
    }

    public String getBy_user_id() {
        return by_user_id;
    }

    public void setBy_user_id(String by_user_id) {
        this.by_user_id = by_user_id;
    }

    public String getCraft() {
        return craft;
    }

    public void setCraft(String craft) {
        this.craft = craft;
    }

    public String getFiltered() {
        return filtered;
    }

    public void setFiltered(String filtered) {
        this.filtered = filtered;
    }

    public String getAlias_whell() {
        return alias_whell;
    }

    public void setAlias_whell(String alias_whell) {
        this.alias_whell = alias_whell;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGetThumb() {
        if(getThumb != null && !getThumb.startsWith("http")) {
            getThumb = BuildConfig.SERVER_ROOT_URL + getThumb;
        }
        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Beer clone(){
        Beer beer=new Beer();
        beer.setId(getId());
        beer.setTitle(getTitle());
        beer.setCountry_id(getCountry_id());
        beer.setBrand_id(getBrand_id());
        beer.setType_id(getType_id());
        beer.setBrewery_id(getBrewery_id());
        beer.setRelations(relations.clone());

        return beer;
    }
}
