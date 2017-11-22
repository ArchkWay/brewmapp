package com.brewmapp.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kras on 22.11.2017.
 */

public class Related_model_data {
    private List<Photo> photo=new ArrayList<>();

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }
}
