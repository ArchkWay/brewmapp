package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by Kras on 16.01.2018.
 */

public class Averagevalue implements Serializable {

    /**
     * evaluation_type_id : 1
     * name : аромат
     * name_en : smell
     * count : 1
     * average_value : 1.5
     */

    private String evaluation_type_id;
    private String name;
    private String name_en;
    private int count;
    private double average_value;
    private Full_information full_information;

    public String getEvaluation_type_id() {
        return evaluation_type_id;
    }

    public void setEvaluation_type_id(String evaluation_type_id) {
        this.evaluation_type_id = evaluation_type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getAverage_value() {
        return average_value;
    }

    public void setAverage_value(double average_value) {
        this.average_value = average_value;
    }
}
