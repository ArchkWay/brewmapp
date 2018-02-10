package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.presentation.view.impl.widget.BeerView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 17.11.2017.
 */

public class FilterBeerInfo extends AdapterItem<Beer, BeerView> {
    private int layoutRes=R.layout.view_beer;
    @Override
    public int getLayoutRes() {
        return layoutRes;
    }

    public void setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
    }
}
