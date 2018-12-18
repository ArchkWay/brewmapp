package com.brewmapp.data;

import android.util.Log;

import com.brewmapp.data.entity.LocalizedStrings;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Roman on 11.11.2018.
 */

public class LocalizedStringsDeserializer implements JsonDeserializer<LocalizedStrings> {

    @Override
    public LocalizedStrings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if(json.isJsonArray()){
            return null;
        }

        JsonObject jObj = json.getAsJsonObject();
        LocalizedStrings strs = new LocalizedStrings();
        try {
//            if (jObj.entrySet().size() == 1 && jObj.has("strings") && jObj.get("strings").isJsonObject()) {
//                jObj = jObj.get("strings").getAsJsonObject();
//            }

            Set<Map.Entry<String, JsonElement>> entries = jObj.entrySet();

            for (Map.Entry<String, JsonElement> entry : entries) {
                String key = entry.getKey();

                try {
                    strs.getStrings().put(key, jObj.get(key).getAsString());
                }
                catch (Exception e){
                    Log.v("xzxz", "---DBG LocalizedStringsDeserializer Exception="+ e.getMessage());
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return strs;
    }
}
