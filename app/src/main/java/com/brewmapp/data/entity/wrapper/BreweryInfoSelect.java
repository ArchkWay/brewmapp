package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BreweryShort;
import com.brewmapp.presentation.view.impl.widget.BreweryView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 05.12.2017.
 */

public class BreweryInfoSelect extends AdapterItem<BreweryShort, BreweryView> implements IFilterable {

    public BreweryInfoSelect(BreweryShort model) {
        super(model);
    }

    public BreweryInfoSelect() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_brewery_filter;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}