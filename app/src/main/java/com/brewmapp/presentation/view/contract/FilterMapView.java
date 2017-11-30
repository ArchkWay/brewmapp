package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.FilterRestoLocation;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by nlbochas on 28/10/2017.
 */

public interface FilterMapView extends BasicView {
    void showRestoFilters(List<FilterRestoField> fieldList);
    void showBeerFilters(List<FilterBeerField> fieldList);
    void setTabActive(int i);
    void goToMap(List<FilterRestoLocation> restoLocations);
    void showProgressBar(boolean show);
}
