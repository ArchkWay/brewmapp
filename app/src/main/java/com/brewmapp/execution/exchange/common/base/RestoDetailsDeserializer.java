package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Distance;
import com.brewmapp.data.entity.Feature;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.Menu;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.RestoType;
import com.brewmapp.data.entity.RestoWorkTime;
import com.brewmapp.data.entity.wrapper.RestoDetailInfo;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Iterator;

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
            restoDetail.setDistance(context.deserialize(((JsonObject) json).getAsJsonObject("distance"),Distance.class));
            restoDetail.setResto(context.deserialize(((JsonObject) json).getAsJsonArray("resto").getAsJsonArray().get(0),Resto.class));
            try {
                Iterator<JsonElement> iterator=((JsonObject) json).getAsJsonArray("resto_kitchen").getAsJsonArray().iterator();
                while (iterator.hasNext())
                    restoDetail.getResto_kitchen().add(context.deserialize(iterator.next(), Kitchen.class));
            }catch (Exception e){}
            try {
                Iterator<JsonElement> iterator=((JsonObject) json).getAsJsonArray("menu").getAsJsonArray().iterator();
                while (iterator.hasNext()) {
                    JsonElement jsonElement=iterator.next();
                    Menu menu=context.deserialize(jsonElement, Menu.class);
                    restoDetail.getMenu().add(menu);
                }}catch ( Exception e){}
            try {
                Iterator<JsonElement> iterator=((JsonObject) json).getAsJsonArray("resto_feature").getAsJsonArray().iterator();
                while (iterator.hasNext())
                    restoDetail.getResto_feature().add(context.deserialize(iterator.next(), Feature.class));
            }catch (Exception e){}
            try {
                Iterator<JsonElement> iterator=((JsonObject) json).getAsJsonArray("resto_type").getAsJsonArray().iterator();
                while (iterator.hasNext())
                    restoDetail.getResto_type().add(context.deserialize(iterator.next(), RestoType.class));
            }catch (Exception e){}
            try {
                Iterator<JsonElement> iterator=((JsonObject) json).getAsJsonArray("resto_work_times").getAsJsonArray().iterator();
                while (iterator.hasNext())
                    restoDetail.getResto_work_tyoe().add(context.deserialize(iterator.next(), RestoWorkTime.class));
            }catch (Exception e){}


            o.setModel(restoDetail);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }
}
