package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.RestoType;
import com.brewmapp.presentation.view.impl.widget.TypeView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 01.11.2017.
 */

public class RestoTypeInfo extends AdapterItem<RestoType, TypeView> implements IFilterable {

    @Override
    public int getLayoutRes() {
        return R.layout.view_type;
    }

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }

    @Override
    public RestoType getModel() {
        return super.getModel();
    }
}