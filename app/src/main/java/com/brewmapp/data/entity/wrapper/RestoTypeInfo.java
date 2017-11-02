package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.RestoType;
import com.brewmapp.presentation.view.impl.widget.RestoTypeView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 01.11.2017.
 */

public class RestoTypeInfo extends AdapterItem<RestoType, RestoTypeView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_sale;
    }
}