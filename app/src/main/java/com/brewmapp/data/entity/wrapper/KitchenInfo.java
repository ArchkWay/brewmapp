package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.presentation.view.impl.widget.KitchenView;
import com.brewmapp.presentation.view.impl.widget.TypeView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 02.11.2017.
 */

public class KitchenInfo extends AdapterItem<Kitchen, KitchenView> {
    @Override
    public int getLayoutRes() {return R.layout.view_kichen_type;}
}