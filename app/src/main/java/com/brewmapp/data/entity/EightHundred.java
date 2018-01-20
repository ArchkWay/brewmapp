package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Kras on 16.01.2018.
 */

class EightHundred implements Serializable {
    private User user;
    @SerializedName("evaluation")
    private EvaluationBeer evaluationBeer;
}
