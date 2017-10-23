package com.brewmapp.data.pojo;

/**
 * Created by xpusher on 10/23/2017.
 */

public class AddInterestPackage {
    private String id;
    private String interestId;
    private String related_model;
    private String related_id;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }



    public String getId() {
        return id;
    }

    public String getInterestId() {
        return interestId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }


    public String getRelated_model() {
        return related_model;
    }

    public void setRelated_model(String related_model) {
        this.related_model = related_model;
    }

}
