package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.presentation.view.impl.widget.PriceRangeView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 03.11.2017.
 */

public class PriceRangeInfo extends AdapterItem<PriceRange, PriceRangeView> implements IFilterable {

    public PriceRangeInfo(PriceRange model) {
        super(model);
    }

    public PriceRangeInfo() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_price_range;
    }

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}
