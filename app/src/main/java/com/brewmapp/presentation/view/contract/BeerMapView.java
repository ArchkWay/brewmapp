package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.FilterRestoLocation;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface BeerMapView extends BasicView {
    void showGeolocationResult(List<FilterRestoLocation> filterRestoLocations);
    void showProgressBar(boolean show);
    void appendItems(List<IFlexible> list);
}
