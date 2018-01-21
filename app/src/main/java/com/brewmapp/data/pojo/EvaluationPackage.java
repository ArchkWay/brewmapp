package com.brewmapp.data.pojo;

/**
 * Created by xpusher on 11/3/2017.
 */

public class EvaluationPackage {
    private String model_id;
    private String ealuation_type;
    private String ealuation_value;

    public EvaluationPackage(String ealuation_type){
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

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }
}
