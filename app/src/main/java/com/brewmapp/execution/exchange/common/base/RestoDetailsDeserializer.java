package com.brewmapp.execution.exchange.common.base;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.data.entity.wrapper.RestoDetailInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Wrapper;
import java.util.List;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 27.10.2017.
 */

public class RestoDetailsDeserializer  implements JsonDeserializer<RestoDetailInfo>{

    @Override
    public RestoDetailInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RestoDetailInfo o=null;
        try {
            o= (RestoDetailInfo) ((Class) typeOfT).newInstance();
            RestoDetail restoDetail=new RestoDetail();
            restoDetail.setResto(context.deserialize(((JsonObject) json).getAsJsonArray("resto").getAsJsonArray().get(0),Resto.class));
            o.setModel(restoDetail);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }
}
