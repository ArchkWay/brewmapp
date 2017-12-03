package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerDensity;
import com.brewmapp.presentation.view.impl.widget.BeerDensityView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerDensityInfo extends AdapterItem<BeerDensity, BeerDensityView> implements IFilterable {

    public BeerDensityInfo(BeerDensity model) {
        super(model);
    }

    public BeerDensityInfo() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_beer_density;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}