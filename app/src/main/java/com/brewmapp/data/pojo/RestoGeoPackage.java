package com.brewmapp.data.pojo;

/**
 * Created by Kras on 16.11.2017.
 */

public class RestoGeoPackage {
    private String beer_id;
    private String coordStart;
    private String coordEnd;

    public String getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(String beer_id) {
        this.beer_id = beer_id;
    }

    public void setCoordStart(String coordStart) {
        this.coordStart = coordStart;
    }

    public String getCoordStart() {
        return coordStart;
    }

    public void setCoordEnd(String coordEnd) {
        this.coordEnd = coordEnd;
    }

    public String getCoordEnd() {
        return coordEnd;
    }
}
