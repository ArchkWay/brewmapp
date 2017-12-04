package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerColor;
import com.brewmapp.presentation.view.impl.widget.BeerColorView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerColorInfo extends AdapterItem<BeerColor, BeerColorView> implements IFilterable {

    public BeerColorInfo(BeerColor model) {
        super(model);
    }

    public BeerColorInfo() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_beer_color;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}