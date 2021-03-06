package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerBrand;
import com.brewmapp.presentation.view.impl.widget.BeerBrandView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 28.11.2017.
 */

public class BeerBrandInfo extends AdapterItem<BeerBrand, BeerBrandView> implements IFilterable {

    public BeerBrandInfo(BeerBrand model) {
        super(model);
    }

    public BeerBrandInfo() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_beer_brand;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}