package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.pojo.BeerTypes;
import com.brewmapp.presentation.view.impl.widget.BeerTypeView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 27.11.2017.
 */

public class BeerTypeInfo extends AdapterItem<BeerTypes, BeerTypeView> implements IFilterable {

    @Override
    public int getLayoutRes() {return R.layout.view_beer_types;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}