package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Interest;

import com.brewmapp.data.entity.Product;
import com.brewmapp.presentation.view.impl.widget.InterestView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestInfo extends AdapterItem<Interest, InterestView> {

    @Override
    public int getLayoutRes() {
        return R.layout.view_interest;
    }

    public InterestInfo(Product product){
        setModel(new Interest(product));
    }

    public InterestInfo(){

    }

}
