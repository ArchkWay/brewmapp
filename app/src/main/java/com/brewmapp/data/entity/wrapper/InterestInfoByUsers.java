package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.presentation.view.impl.widget.InterestView;
import com.brewmapp.presentation.view.impl.widget.InterestViewByUsers;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestInfoByUsers extends AdapterItem<Interest, InterestViewByUsers> {

    @Override
    public int getLayoutRes() {
        return R.layout.view_interest;
    }

    public InterestInfoByUsers(){

    }

}
