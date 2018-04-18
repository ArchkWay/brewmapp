package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.presentation.view.impl.widget.InterestAddViewResto;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 25.10.2017.
 */

public class SearchRestoInfo extends AdapterItem<Resto, InterestAddViewResto> {
    // mode 0 - item for select resto(default)
    // mode 1 - item for create resto

    private int mode=0;

    public SearchRestoInfo(){

    }

    public SearchRestoInfo(Resto resto,int mode){
        this.mode=mode;
        setModel(resto);

    }

    @Override
    public int getLayoutRes() {
        if(mode==0)
            return R.layout.view_select_resto;
        else
            return R.layout.view_creae_resto;

    }
}
