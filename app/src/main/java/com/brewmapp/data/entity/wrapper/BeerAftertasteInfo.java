package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerAftertaste;
import com.brewmapp.presentation.view.impl.widget.BeerAftertasteView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerAftertasteInfo extends AdapterItem<BeerAftertaste, BeerAftertasteView> implements IFilterable {

    public BeerAftertasteInfo(BeerAftertaste model) {
        super(model);
    }

    public BeerAftertasteInfo() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_beer_aftertaste;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}