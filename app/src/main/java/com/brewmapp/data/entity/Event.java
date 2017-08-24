package com.brewmapp.data.entity;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ovcst on 24.08.2017.
 */

public class Event {
    private int id;
    private String name, text;

    @SerializedName(Keys.SHORT_TEXT)
    private String shortText;

    private int type;

    @SerializedName(Keys.LOCATION_ID)
    private int locationId;

    @SerializedName(Keys.DATE_FROM)
    private Date dateFrom;

    @SerializedName(Keys.DATE_TO)
    private Date dateTo;

    @SerializedName(Keys.TIME_FROM)
    private String timeFrom;

    @SerializedName(Keys.TIME_TO)
    private String timeTo;

    private BeerLocation location;

    @SerializedName(Keys.RESTO_INFO)
    private Resto resto;

    @SerializedName(Keys.AVG_BALL)
    private Ball ball;

    private int like;

    @SerializedName(Keys.DIS_LIKE)
    private int dislike;

    private int interested;

    @SerializedName(Keys.NO_INTERESTED)
    private int noInterested;

    @SerializedName(Keys.I_WILL_GO)
    private int iWillGo;

    private int invited;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getShortText() {
        return shortText;
    }

    public int getType() {
        return type;
    }

    public int getLocationId() {
        return locationId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public BeerLocation getLocation() {
        return location;
    }

    public Resto getResto() {
        return resto;
    }

    public Ball getBall() {
        return ball;
    }

    public int getLike() {
        return like;
    }

    public int getDislike() {
        return dislike;
    }

    public int getInterested() {
        return interested;
    }

    public int getNoInterested() {
        return noInterested;
    }

    public int getiWillGo() {
        return iWillGo;
    }

    public int getInvited() {
        return invited;
    }
}
