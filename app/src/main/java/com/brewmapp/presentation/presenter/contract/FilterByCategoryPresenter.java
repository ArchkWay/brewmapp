package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.data.pojo.ScrollPackage;
import com.brewmapp.presentation.view.contract.FilterByCategoryView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by nixus on 01.11.2017.
 */

public interface FilterByCategoryPresenter extends LivePresenter<FilterByCategoryView> {
    void loadRestoTypes();
    void loadKitchenTypes();
    void loadPriceRangeTypes(String type);
    void loadFeatureTypes();
    void sendQueryFullSearch(FullSearchPackage fullSearchPackage);
    void loadBeerTypes();
    void loadBeerPack();
    void loadBeerBrand(FullSearchPackage fullSearchPackage);
    void loadBeerColor();
    void loadBeerTaste();
    void loadBeerSmell();
    void loadBeerAfterTaste();
    void loadBeerPower();
    void loadBeerDensity();
    void loadBeerIbu();
    void loadCountries();
    void loadRegions(GeoPackage geoPackage);
    void loadCity(GeoPackage geoPackage);
}
