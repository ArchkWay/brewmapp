package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by Kras on 16.01.2018.
 */

class Evaluation1 implements Serializable{

    /**
     * id : 75
     * product_model : Beer
     * product_id : 1
     * user_id : 800
     * evaluation_type : 1
     * evaluation_value : 1.50
     * created_at : 2018-01-16 15:38:23
     */

    private String id;
    private String product_model;
    private String product_id;
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

    public String getProduct_model() {
        return product_model;
    }

    public void setProduct_model(String product_model) {
        this.product_model = product_model;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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
