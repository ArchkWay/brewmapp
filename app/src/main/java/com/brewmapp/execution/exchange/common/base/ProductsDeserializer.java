package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Product;
import com.brewmapp.data.entity.wrapper.ProductInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 21.10.2017.
 */

public class ProductsDeserializer extends AdapterItemDeserializer<Product, ProductInfo> {
    @Override
    protected Class<Product> provideType() {
        return Product.class;
    }

    @Override
    protected Class<ProductInfo> provideWrapperType() {
        return ProductInfo.class;
    }
}
