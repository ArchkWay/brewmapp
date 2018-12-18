package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.presentation.view.contract.SelectCategoryActivityView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by nixus on 01.11.2017.
 */

public interface SelectCategoryActivityPresenter extends LivePresenter<SelectCategoryActivityView> {
    void loadNewsRelatedModels();
    void loadReviewsRelatedModels();
    void loadRestoTypes();
    void loadKitchenTypes();
    void loadPriceRangeTypes(String type);
    void loadFeatureTypes();
    void loadBeerTypes(FullSearchPackage fullSearchPackage);
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
    void loadBrewery();
    void loadBreweryCategoryItem(int filterCategory, FullSearchPackage searchPackage);
    void loadRestoCategoryItem(int filterCategory, FullSearchPackage searchPackage);
    void loadBeerCategoryItem(int filterCategory, FullSearchPackage searchPackage);

    void sendQueryFullSearch(FullSearchPackage fullSearchPackage);
    void sendQueryCitySearch(FullSearchPackage fullSearchPackage);
    void sendQueryCountrySearch(FullSearchPackage fullSearchPackage);

    void loadFilter();
}
