package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterOnMapResto;
import com.brewmapp.presentation.view.impl.widget.OnMapRestoFilterView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 04.12.2017.
 */

public class FilterRestoLocationInfo extends AdapterItem<FilterOnMapResto, OnMapRestoFilterView> {

    @Override
    public int getLayoutRes() {
        return R.layout.view_resto_om_map;
    }
}
