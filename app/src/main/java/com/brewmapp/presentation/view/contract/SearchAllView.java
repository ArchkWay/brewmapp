package com.brewmapp.presentation.view.contract;

import android.content.Intent;

import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface SearchAllView extends BasicView {
    void showRestoFilters(List<FilterRestoField> fieldList);
    void showBeerFilters(List<FilterBeerField> fieldList);
    void showBreweryFilters(List<FilterBreweryField> fieldList);
    void commonError(String... strings);
}
