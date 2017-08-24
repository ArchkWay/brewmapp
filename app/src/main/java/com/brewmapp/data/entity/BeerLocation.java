package com.brewmapp.data.entity;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ovcst on 24.08.2017.
 */

public class BeerLocation {

    private LocationInfo info;

    @SerializedName(Keys.CITY_ID)
    private String city;

    private MetroInfo metro;

    @SerializedName(Keys.USER_INFO)
    private User user;

    public String getCity() {
        return city;
    }

    public MetroInfo getMetro() {
        return metro;
    }

    public LocationInfo getInfo() {
        return info;
    }

    public static class LocationInfo {
        private int id;

        @SerializedName(Keys.COUNTRY_ID)
        private int countryId;

        @SerializedName(Keys.REGION_ID)
        private int regionId;

        @SerializedName(Keys.CITY_ID)
        private int cityId;

        @SerializedName(Keys.METRO_ID)
        private int metroId;

        private String street;
        private String house;
        private String name;
        private double lat, lng;

        public int getId() {
            return id;
        }

        public int getCountryId() {
            return countryId;
        }

        public int getRegionId() {
            return regionId;
        }

        public int getCityId() {
            return cityId;
        }

        public int getMetroId() {
            return metroId;
        }

        public String getStreet() {
            return street;
        }

        public String getHouse() {
            return house;
        }

        public String getName() {
            return name;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

    public static class MetroInfo {
        private int id;

        @SerializedName(Keys.CITY_ID)
        private int cityId;

        private String name;
        private double lat, lon;
        private int distance;

        public int getId() {
            return id;
        }

        public int getCityId() {
            return cityId;
        }

        public String getName() {
            return name;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public int getDistance() {
            return distance;
        }
    }
}
