package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Feature;
import com.brewmapp.presentation.view.impl.widget.FeatureView;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 03.11.2017.
 */

public class FeatureInfo extends AdapterItem<Feature, FeatureView> implements IFilterable {

    public FeatureInfo(Feature model) {
        super(model);
    }

    public FeatureInfo() {
    }

    @Override
    public int getLayoutRes() {return R.layout.view_features;}

    @Override
    public boolean filter(String constraint) {
        return getModel().getName().toLowerCase().contains(constraint);
    }

}