package com.brewmapp.data.pojo;

import java.io.Serializable;

/**
 * Created by oleg on 02.10.17.
 */

public class GeolocatorResultPackage implements Serializable {
    private String address;
    private ru.frosteye.ovsa.data.entity.SimpleLocation location;

    public GeolocatorResultPackage(String address, ru.frosteye.ovsa.data.entity.SimpleLocation location) {
        this.address = address;
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public ru.frosteye.ovsa.data.entity.SimpleLocation getLocation() {
        return location;
    }
}
