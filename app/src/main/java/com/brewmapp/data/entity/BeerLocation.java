package com.brewmapp.data.entity;

import com.brewmapp.data.LocalizedStringsDeserializer;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ovcst on 24.08.2017.
 */

public class BeerLocation {


    @SerializedName(Keys.LOCATION)
    private LocationInfo info;

    @JsonAdapter (LocalizedStringsDeserializer.class)
    @SerializedName(Keys.CITY_ID)
    private LocalizedStrings cityId;

    private MetroInfo metro;

    @SerializedName(Keys.USER_INFO)
    private User user;

    public String getCity() {
        return cityId != null ? cityId.toString() : null;
    }

    public MetroInfo getMetro() {
        return metro;
    }

    public LocationInfo getInfo() {
        return info;
    }

    public String getFormattedAddress() {
        if(info != null) {
            return String.format("%s, %s", cityId != null ? cityId.toString() : "" , info.getFormattedAddress());
        } else {
            return cityId.toString();
        }
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

        private String mStreet;
        private String mHouse;
        private String name;
        private double lat, lon;

        public String getFormattedAddress() {
            LocationInfo info = this;
            StringBuilder builder = new StringBuilder();
            builder.append(info.getStreet());
            if(info.getHouse() != null && !info.getHouse().isEmpty()) {
                builder.append(", ").append(info.getHouse());
            }
            return builder.toString();
        }

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
            return mStreet;
        }

        public String getHouse() {
            return mHouse;
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
    }

    public static class MetroInfo {
        private int id;

        @SerializedName(Keys.CITY_ID)
        private int cityId;

        private String mName;
        private double lat, lon;
        private int distance;

        public int getId() {
            return id;
        }

        public int getCityId() {
            return cityId;
        }

        public String getName() {
            return mName;
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

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}
