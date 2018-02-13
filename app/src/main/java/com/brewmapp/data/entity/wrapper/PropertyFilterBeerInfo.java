package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.PropertyFilterBeer;
import com.brewmapp.presentation.view.impl.widget.PropertyFilterBeerView;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 13.02.2018.
 */

public class PropertyFilterBeerInfo extends AdapterItem<PropertyFilterBeer, PropertyFilterBeerView> {
    public PropertyFilterBeerInfo(PropertyFilterBeer propertyFilterBeer) {
        setModel(propertyFilterBeer);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_property_filter;
    }
}
