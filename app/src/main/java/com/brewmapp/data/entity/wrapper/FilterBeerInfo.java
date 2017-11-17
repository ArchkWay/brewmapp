package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.presentation.view.impl.widget.BeerView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 17.11.2017.
 */

public class FilterBeerInfo extends AdapterItem<Beer, BeerView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_beer;
    }
}
