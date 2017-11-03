package com.brewmapp.data.entity;

/**
 * Created by xpusher on 11/3/2017.
 */

public class Evaluation {
    private String id;
    private String resto_id;
    private String user_id;
    private String evaluation_type;
    private String evaluation_value;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResto_id() {
        return resto_id;
    }

    public void setResto_id(String resto_id) {
        this.resto_id = resto_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEvaluation_type() {
        return evaluation_type;
    }

    public void setEvaluation_type(String evaluation_type) {
        this.evaluation_type = evaluation_type;
    }

    public String getEvaluation_value() {
        return evaluation_value;
    }

    public void setEvaluation_value(String evaluation_value) {
        this.evaluation_value = evaluation_value;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
