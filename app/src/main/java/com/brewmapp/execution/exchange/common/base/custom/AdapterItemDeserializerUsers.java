package com.brewmapp.execution.exchange.common.base.custom;

import com.brewmapp.data.entity.Relations;
import com.brewmapp.data.entity.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by xpusher on 12/4/2017.
 */

public abstract class AdapterItemDeserializerUsers<T, Wrapper extends AdapterItem<T, ?>>
        implements JsonDeserializer<Wrapper> {

    @Override
    public final Wrapper deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {

        try {
            T object = context.deserialize(json, provideType());
            Wrapper wrapper = provideWrapperType().newInstance();
            try {
                ((User)object).setRelations_fix_bugs_backend(context.deserialize(((JsonObject) json).getAsJsonObject("relations"),Relations.class));
            }catch (Exception e){}

            wrapper.setModel(object);
            return wrapper;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected abstract Class<T> provideType();
    protected abstract Class<Wrapper> provideWrapperType();
}
