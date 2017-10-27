package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.wrapper.RestoDetailInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Wrapper;
import java.util.List;

/**
 * Created by Kras on 27.10.2017.
 */

public class RestoDetails extends ListResponse<RestoDetailInfo> {
    public RestoDetails(@NonNull List<RestoDetailInfo> models) {
        super(models);
    }
}
