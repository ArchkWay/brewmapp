package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.presentation.view.impl.widget.EventView;
import com.brewmapp.presentation.view.impl.widget.SaleView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 16.08.17.
 */

public class SaleInfo extends AdapterItem<Sale, SaleView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_sale;
    }
}
