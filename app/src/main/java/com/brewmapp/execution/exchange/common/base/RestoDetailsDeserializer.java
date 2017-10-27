package com.brewmapp.execution.exchange.common.base;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Wrapper;
import java.util.List;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 27.10.2017.
 */

public class RestoDetailsDeserializer extends ListResponse<RestoDetail> implements JsonDeserializer<Wrapper> {
    public RestoDetailsDeserializer(@NonNull List<RestoDetail> models) {
        super(models);
    }

    @Override
    public Wrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
