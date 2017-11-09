package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Feature;
import com.brewmapp.presentation.view.impl.widget.FeatureView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 03.11.2017.
 */

public class FeatureInfo extends AdapterItem<Feature, FeatureView> {
    @Override
    public int getLayoutRes() {return R.layout.view_features;}
}