package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerPack;
import com.brewmapp.presentation.view.impl.widget.BeerPackView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 28.11.2017.
 */

public class BeerPackInfo extends AdapterItem<BeerPack, BeerPackView> implements IFilterable {

    public BeerPackInfo(BeerPack model) {
        super(model);
    }

    public BeerPackInfo() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_beer_pack;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}