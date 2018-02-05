package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.presentation.view.impl.widget.BrewerySearchView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 16.12.2017.
 */

public class BreweryInfo extends AdapterItem<Brewery, BrewerySearchView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_search_brewery;
    }
}
