package com.brewmapp.data.entity;

import com.brewmapp.data.LocalizedStringsDeserializer;
import com.google.gson.annotations.JsonAdapter;

/**
 * Created by xpusher on 11/7/2017.
 */

public class AverageEvaluation {
    private String evaluation_type_id;

    @JsonAdapter (LocalizedStringsDeserializer.class)
    private LocalizedStrings name;

    private String name_en;
    private String count;
    private String average_value;


    public String getEvaluation_type_id() {
        return evaluation_type_id;
    }

    public void setEvaluation_type_id(String evaluation_type_id) {
        this.evaluation_type_id = evaluation_type_id;
    }

    public String getName() {
        return name.toString();
    }

    public void setName(String name) {
//        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAverage_value() {
        return average_value;
    }

    public void setAverage_value(String average_value) {
        this.average_value = average_value;
    }

}
