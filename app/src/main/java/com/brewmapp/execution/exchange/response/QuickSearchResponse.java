package com.brewmapp.execution.exchange.response;

import com.brewmapp.data.entity.BeerLocation;
import com.brewmapp.data.entity.Models;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleg on 30.09.17.
 */

public class QuickSearchResponse {

    private int total;

    private Models models;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Models getModels() {
        return models;
    }

    public void setModels(Models models) {
        this.models = models;
    }
}
