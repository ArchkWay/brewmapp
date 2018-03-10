package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterRestoOnMap;
import com.brewmapp.presentation.view.impl.widget.OnMapRestoFilterView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 04.12.2017.
 */

public class FilterRestoLocationInfo extends AdapterItem<FilterRestoOnMap, OnMapRestoFilterView> implements IFilterable{

    public FilterRestoLocationInfo(){

    }

    public FilterRestoLocationInfo(FilterRestoOnMap filterRestoOnMap) {
        setModel(filterRestoOnMap);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_resto_om_map;
    }

    @Override
    public boolean filter(String constraint) {
        try {
            return getModel().getName().toLowerCase().contains(constraint.toLowerCase());
        }catch (Exception e){
            return false;
        }
    }
}
