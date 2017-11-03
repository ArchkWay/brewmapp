package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Feature;
import com.brewmapp.data.entity.wrapper.FeatureInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 03.11.2017.
 */

public class FeatureDeserializer extends AdapterItemDeserializer<Feature, FeatureInfo> {
    @Override
    protected Class<Feature> provideType() {
        return Feature.class;
    }

    @Override
    protected Class<FeatureInfo> provideWrapperType() {
        return FeatureInfo.class;
    }
}