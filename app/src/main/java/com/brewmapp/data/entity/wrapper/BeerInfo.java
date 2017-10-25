package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.presentation.view.impl.widget.InterestAddViewBeer;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 21.10.2017.
 */

public class BeerInfo extends AdapterItem<Beer, InterestAddViewBeer> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_beer;
    }
}
