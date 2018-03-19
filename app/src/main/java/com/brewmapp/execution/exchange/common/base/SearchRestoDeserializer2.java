package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Distance;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.Metro;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.SearchRestoInfo;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kras on 21.10.2017.
 */

public class SearchRestoDeserializer2 implements JsonDeserializer<SearchRestoInfo> {

    @Override
    public SearchRestoInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SearchRestoInfo searchRestoInfo;
        try {
            searchRestoInfo= (SearchRestoInfo) ((Class) typeOfT).newInstance();
            searchRestoInfo.setModel(context.deserialize(json, Resto.class));
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //region Metro problem resolve
        try {
            if(((JsonObject)((JsonObject) json).get("distance")).get("metro").isJsonArray()){
                List<Metro> metroList=new ArrayList<>();
                Iterator<JsonElement> iterator=((JsonObject)((JsonObject) json).get("distance")).get("metro").getAsJsonArray().iterator();
                while (iterator.hasNext())
                    metroList.add(context.deserialize(iterator.next(), Metro.class));
                searchRestoInfo.getModel().getDistance().setMetro(metroList);
            }
        }catch (Exception e){}
        //endregion

        return searchRestoInfo;

    }
}
