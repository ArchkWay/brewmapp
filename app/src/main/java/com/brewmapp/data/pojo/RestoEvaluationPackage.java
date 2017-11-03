package com.brewmapp.data.pojo;

/**
 * Created by xpusher on 11/3/2017.
 */

public class RestoEvaluationPackage {
    private String resto_id;
    private String ealuation_type;
    private String ealuation_value;

    public RestoEvaluationPackage(String ealuation_type){
        setEaluation_type(ealuation_type);
    }

    public String getEaluation_type() {
        return ealuation_type;
    }

    public void setEaluation_type(String ealuation_type) {
        this.ealuation_type = ealuation_type;
    }

    public String getEaluation_value() {
        return ealuation_value;
    }

    public void setEaluation_value(String ealuation_value) {
        this.ealuation_value = ealuation_value;
    }

    public String getResto_id() {
        return resto_id;
    }

    public void setResto_id(String resto_id) {
        this.resto_id = resto_id;
    }
}
