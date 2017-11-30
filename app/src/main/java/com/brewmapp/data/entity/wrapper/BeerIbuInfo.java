package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerIbu;
import com.brewmapp.presentation.view.impl.widget.BeerIbuView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerIbuInfo extends AdapterItem<BeerIbu, BeerIbuView> implements IFilterable {

    @Override
    public int getLayoutRes() {return R.layout.view_beer_ibu;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}