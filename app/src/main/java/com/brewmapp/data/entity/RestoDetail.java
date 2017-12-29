package com.brewmapp.data.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kras on 27.10.2017.
 */

public class RestoDetail implements Serializable{

    private Resto resto=new Resto();
    private List<Kitchen> resto_kitchen=new ArrayList<>();
    private List<Menu> menu=new ArrayList<>();
    private List<Feature> resto_feature=new ArrayList<>();
    private List<RestoType> resto_type=new ArrayList<>();
    private List<RestoWorkTime> resto_work_tyoe=new ArrayList<>();

    public void setResto_kitchen(List<Kitchen> resto_kitchen) {
        this.resto_kitchen = resto_kitchen;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public List<Feature> getResto_feature() {
        return resto_feature;
    }

    public void setResto_feature(List<Feature> resto_feature) {
        this.resto_feature = resto_feature;
    }

    public List<RestoType> getResto_type() {
        return resto_type;
    }

    public void setResto_type(List<RestoType> resto_type) {
        this.resto_type = resto_type;
    }



    public List<RestoWorkTime> getResto_work_tyoe() {
        return resto_work_tyoe;
    }

    public void setResto_work_tyoe(List<RestoWorkTime> resto_work_tyoe) {
        this.resto_work_tyoe = resto_work_tyoe;
    }



    public List<Kitchen> getResto_kitchen() {
        return resto_kitchen;
    }

    public Resto getResto() {
        return resto;
    }

    public void setResto(Resto resto) {
        this.resto = resto;
    }

    public RestoDetail clone(){
        RestoDetail restoDetail=new RestoDetail();
        restoDetail.setResto(new Resto());
        restoDetail.getResto().setName(getResto().getName());
        restoDetail.getResto().setAvgCost(getResto().getAvgCost());
        restoDetail.getResto().setId(getResto().getId());
        restoDetail.getResto().setLocation(getResto().getLocation().clone());

        return restoDetail;
    }

    public String getFormattedKitchen(){
        StringBuilder stringBuilder=new StringBuilder();
        for (Kitchen kitchen:getResto_kitchen())
            stringBuilder.append(kitchen.getName()).append(", ");
        String result=stringBuilder.toString();
        result=result.trim().replaceAll("[,]$", "");
            return result;
    }

}
