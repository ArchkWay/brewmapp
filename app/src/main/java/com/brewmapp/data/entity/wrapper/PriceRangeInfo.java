package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.RestoType;
import com.brewmapp.presentation.view.impl.widget.PriceRangeView;
import com.brewmapp.presentation.view.impl.widget.TypeView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 03.11.2017.
 */

public class PriceRangeInfo extends AdapterItem<PriceRange, PriceRangeView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_price_range;
    }
}
