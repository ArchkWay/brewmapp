package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.presentation.view.impl.widget.InterestAddViewResto;
import com.brewmapp.presentation.view.impl.widget.ItemShowAllRestoView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 16.01.2018.
 */

public class ItemShowAllResto extends AdapterItem<Void, ItemShowAllRestoView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_item_show_all_resto;
    }
}
