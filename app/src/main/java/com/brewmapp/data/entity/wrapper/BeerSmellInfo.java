package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerSmell;
import com.brewmapp.presentation.view.impl.widget.BeerSmellView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerSmellInfo extends AdapterItem<BeerSmell, BeerSmellView> implements IFilterable {

    public BeerSmellInfo(BeerSmell model) {
        super(model);
    }

    public BeerSmellInfo() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_beer_smell;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}