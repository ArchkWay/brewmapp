package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.RestoDetail;
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

public class RestoDetails extends ListResponse<RestoDetail> implements JsonDeserializer<Wrapper> {
    public RestoDetails(@NonNull List<RestoDetail> models) {
        super(models);
    }

    @Override
    public Wrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
