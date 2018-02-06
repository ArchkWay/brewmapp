package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.City;
import com.brewmapp.presentation.view.impl.widget.CityView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 02.12.2017.
 */

public class CityInfo extends AdapterItem<City, CityView> implements IFilterable {
    public CityInfo(City model) {
        super(model);
    }

    public CityInfo() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_city;
    }

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}