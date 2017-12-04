package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.entity.wrapper.SaleInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by oleg on 16.08.17.
 */

public class SalesDeserializer extends AdapterItemDeserializer<Sale, SaleInfo> {
    @Override
    protected Class<Sale> provideType() {
        return Sale.class;
    }

    @Override
    protected Class<SaleInfo> provideWrapperType() {
        return SaleInfo.class;
    }
}
