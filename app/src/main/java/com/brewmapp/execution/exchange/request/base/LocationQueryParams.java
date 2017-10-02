package com.brewmapp.execution.exchange.request.base;

import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 30.09.17.
 */

public class LocationQueryParams extends RequestParams {

    public LocationQueryParams() {
    }

    public LocationQueryParams(double lat, double lng) {
        addParam(Keys.LAT, lat);
        addParam(Keys.LON, lng);
    }

    public LocationQueryParams withLimits(int start, int end) {
        addParam(Keys.LIMIT_START, start);
        addParam(Keys.LIMIT_END, end);
        return this;
    }

    public LocationQueryParams withUserId(int userId) {
        addParam(Keys.USER_ID, userId);
        return this;
    }
}
