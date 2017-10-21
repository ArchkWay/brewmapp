package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Product;
import com.brewmapp.presentation.view.contract.AddFavoriteBeerView;
import com.brewmapp.presentation.view.impl.widget.ProductView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 21.10.2017.
 */

public class ProductInfo extends AdapterItem<Product, ProductView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_product;
    }
}
