package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.Product;
import com.brewmapp.data.entity.wrapper.ProductInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by Kras on 21.10.2017.
 */

public class Products extends ListResponse<ProductInfo> {
    public Products(@NonNull List<ProductInfo> models) {
        super(models);
    }
}
