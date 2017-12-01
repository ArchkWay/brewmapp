package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Region;
import com.brewmapp.presentation.view.impl.widget.RegionView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 02.12.2017.
 */

public class RegionInfo extends AdapterItem<Region, RegionView> implements IFilterable {
    @Override
    public int getLayoutRes() {return R.layout.view_region;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }
}