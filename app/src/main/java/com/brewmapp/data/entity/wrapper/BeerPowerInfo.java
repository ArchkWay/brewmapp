package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerPower;
import com.brewmapp.presentation.view.impl.widget.BeerPowerView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerPowerInfo extends AdapterItem<BeerPower, BeerPowerView> implements IFilterable {

    @Override
    public int getLayoutRes() {return R.layout.view_beer_power;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}