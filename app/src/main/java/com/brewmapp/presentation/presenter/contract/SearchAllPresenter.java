package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.view.contract.SearchAllView;

import java.util.List;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface SearchAllPresenter extends LivePresenter<SearchAllView> {
    void storeTabActive(int position);
    void saveRestoFilterChanges(List<FilterRestoField> fields);
    void saveBeerFilterChanges(List<FilterBeerField> fields);
    void saveBreweryFilterChanges(List<FilterBreweryField> fields);

}
