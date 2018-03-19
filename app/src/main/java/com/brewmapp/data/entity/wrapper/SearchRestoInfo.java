package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.presentation.view.impl.widget.InterestAddViewResto;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 25.10.2017.
 */

public class SearchRestoInfo extends AdapterItem<Resto, InterestAddViewResto> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_add_resto;
    }
}
