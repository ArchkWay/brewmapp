package com.brewmapp.presentation.presenter.contract;

import android.location.Location;

import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.view.contract.SearchAllView;

import java.util.List;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface SearchFragmentPresenter extends LivePresenter<SearchAllView> {
    void setTabActive(int position);
    void setUserLocation(Location location);
}
