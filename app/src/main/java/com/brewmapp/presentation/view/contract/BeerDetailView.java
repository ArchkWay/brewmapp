package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerDetail;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 30.10.2017.
 */

public interface BeerDetailView extends BasicView {
    void setModel(BeerDetail beerDetail);
}