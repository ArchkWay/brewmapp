package com.brewmapp.data.entity;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ovcst on 24.08.2017.
 */

public class Ball {
    private float rating;

    @SerializedName(Keys.COEFFICIRNT_REVIEW)
    private int coefficientReview;

    @SerializedName(Keys.COEFFICIRNT_LIKE)
    private int coefficientLike;

    public float getRating() {
        return rating;
    }

    public int getCoefficientReview() {
        return coefficientReview;
    }

    public int getCoefficientLike() {
        return coefficientLike;
    }
}
