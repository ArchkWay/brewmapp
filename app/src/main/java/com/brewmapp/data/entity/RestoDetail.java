package com.brewmapp.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kras on 27.10.2017.
 */

public class RestoDetail {

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


}
