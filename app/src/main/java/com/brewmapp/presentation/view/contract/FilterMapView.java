package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.FilterField;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by nlbochas on 28/10/2017.
 */

public interface FilterMapView extends BasicView {
    void showFilters(List<FilterField> fieldList);
}
