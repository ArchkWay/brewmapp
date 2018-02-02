package com.brewmapp.data.pojo;

import com.brewmapp.execution.exchange.request.base.WrapperParams;

import java.util.HashMap;
import java.util.Map;

import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 30.09.17.
 */

public class SearchPackage extends BasePackage{
    private int startLimit, endLimit;
    private double lat, lng;
    private String text;
    private Map<String, String> additionalQueryParams = new HashMap<>();
    private Map<String, String> additionalFields = new HashMap<>();

    public SearchPackage(int startLimit, int endLimit) {
        this.startLimit = startLimit;
        this.endLimit = endLimit;
    }

    public SearchPackage(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public SearchPackage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public SearchPackage setText(String text) {
        this.text = text;
        return this;
    }

    public SearchPackage setStartLimit(int startLimit) {
        this.startLimit = startLimit;
        return this;
    }

    public SearchPackage setEndLimit(int endLimit) {
        this.endLimit = endLimit;
        return this;
    }

    public SearchPackage setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public Map<String, String> getAdditionalQueryParams() {
        return additionalQueryParams;
    }

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    public SearchPackage setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public int getStartLimit() {
        return startLimit;
    }

    public int getEndLimit() {
        return endLimit;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
