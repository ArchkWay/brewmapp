package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Country;
import com.brewmapp.presentation.view.impl.widget.CountryView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 02.12.2017.
 */

public class CountryInfo extends AdapterItem<Country, CountryView> implements IFilterable {

    public CountryInfo(Country model) {
        super(model);
    }

    public CountryInfo() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_country;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }

}