package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.SearchBeer;
import com.brewmapp.presentation.view.impl.widget.SearchBeerView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by nixus on 07.12.2017.
 */

public class SearchBeerInfo extends AdapterItem<SearchBeer, SearchBeerView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_search_beer;
    }
}
