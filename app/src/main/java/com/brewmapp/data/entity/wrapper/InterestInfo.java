package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Interest;

import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.presentation.view.impl.widget.InterestView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestInfo extends AdapterItem<Interest, InterestView> {

    public InterestInfo(Resto resto) {
        setModel(new Interest(resto));
    }

    public InterestInfo(Interest interest) {
        setModel(interest);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_interest;
    }

    public InterestInfo(Beer beer){
        setModel(new Interest(beer));
    }

    public InterestInfo(){

    }

}
