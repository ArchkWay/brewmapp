package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Models;
import com.brewmapp.data.entity.Post;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Kras on 25.11.2017.
 */

public class QuickSearchDeserializer implements JsonDeserializer<Wrapper> {
    @Override
    public Wrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Models models=new Models();

        JsonElement jsonElement=((JsonObject) json).get("News");
        if(jsonElement!=null)
            if(jsonElement.isJsonArray()){
                Iterator<JsonElement> iterator=jsonElement.getAsJsonArray().iterator();
                while (iterator.hasNext())
                    models.getPosts().add(context.deserialize(iterator.next(),Post.class));
            }else if(jsonElement.isJsonObject()){
                Iterator<Map.Entry<String,JsonElement>> iterator =((JsonObject) json).get("News").getAsJsonObject().entrySet().iterator();
                while (iterator.hasNext())
                    models.getPosts().add(context.deserialize(iterator.next().getValue(), Post.class));
            }

        jsonElement=((JsonObject) json).get("Event");
            if(jsonElement!=null)
                if(jsonElement.isJsonArray()) {
                    Iterator<JsonElement> iterator=jsonElement.getAsJsonArray().iterator();
                    while (iterator.hasNext())
                        models.getEvents().add(context.deserialize(iterator.next(),Event.class));
                }else if(jsonElement.isJsonObject()){
                    Iterator<Map.Entry<String,JsonElement>> iterator =((JsonObject) json).get("Event").getAsJsonObject().entrySet().iterator();
                    while (iterator.hasNext())
                        models.getEvents().add(context.deserialize(iterator.next().getValue(), Event.class));
                }

        return models;
    }





}
