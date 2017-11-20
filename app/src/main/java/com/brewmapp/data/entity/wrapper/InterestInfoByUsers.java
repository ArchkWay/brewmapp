package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.presentation.view.impl.widget.WhoIsInterestedView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestInfoByUsers extends AdapterItem<Interest, WhoIsInterestedView> {

    @Override
    public int getLayoutRes() {
        return R.layout.view_who_is_interested;
    }

    public InterestInfoByUsers(){

    }

}
