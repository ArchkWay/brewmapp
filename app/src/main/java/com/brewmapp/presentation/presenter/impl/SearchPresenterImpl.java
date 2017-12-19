package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import javax.inject.Inject;

import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.pojo.BreweryPackage;
import com.brewmapp.data.pojo.FilterBeerPackage;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.task.RestosSearchTask;
import com.brewmapp.execution.task.SearchBeerTask;
import com.brewmapp.execution.task.SearchBreweryTask;
import com.brewmapp.presentation.view.contract.SearchView;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.SearchPresenter;
import java.util.List;

public class SearchPresenterImpl extends BasePresenter<SearchView> implements SearchPresenter {

    private RestosSearchTask loadRestosList;
    private SearchBeerTask loadBeerTask;
    private SearchBreweryTask loadBreweryTask;
    private Context context;

    @Inject
    public SearchPresenterImpl(Context context, RestosSearchTask loadRestosList,
                               SearchBeerTask loadBeerTask, SearchBreweryTask loadBreweryTask) {
        this.context = context;
        this.loadRestosList = loadRestosList;
        this.loadBeerTask = loadBeerTask;
        this.loadBreweryTask = loadBreweryTask;
    }

    @Override
    public void onAttach(SearchView searchView) {
        super.onAttach(searchView);
        Paper.init(context);
    }

    @Override
    public void onDestroy() {
        loadBeerTask.cancel();
        loadRestosList.cancel();
        loadBreweryTask.cancel();
    }

    @Override
    public void loadRestoList(int specialOffer, FullSearchPackage searchPackage) {
        loadRestosList.cancel();
        List<FilterRestoField> filterRestoFields = Paper.book().read("restoCategoryList");
        FilterRestoPackage filterRestoPackage = new FilterRestoPackage();
        filterRestoPackage.setPage(searchPackage.getPage());
        filterRestoPackage.setRestoCity(filterRestoFields.get(FilterRestoField.CITY).getSelectedItemId());
        filterRestoPackage.setRestoTypes(filterRestoFields.get(FilterRestoField.TYPE).getSelectedItemId());
        filterRestoPackage.setMenuBeer(filterRestoFields.get(FilterRestoField.BEER).getSelectedItemId());
        filterRestoPackage.setRestoKitchens(filterRestoFields.get(FilterRestoField.KITCHEN).getSelectedItemId());
        filterRestoPackage.setRestoAveragepriceRange(filterRestoFields.get(FilterRestoField.PRICE).getSelectedItemId());
        filterRestoPackage.setRestoFeatures(filterRestoFields.get(FilterRestoField.FEATURES).getSelectedItemId());
        filterRestoPackage.setResto_discount(specialOffer);
        loadRestosList.execute(filterRestoPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                showError(e.getMessage());
                view.hideProgressBar();
            }

            @Override
            public void onNext(List<IFlexible> restoLocations) {
                view.hideProgressBar();
                if (restoLocations.size() == 0) {
                    view.showMessage("Не найдено ни одного заведения",0);
                } else {
                    view.appendItems(restoLocations);
                }
            }

            @Override
            public void onComplete() {
                view.hideProgressBar();
                super.onComplete();
            }
        });
    }

    @Override
    public void loadBeerList(int craftBeer, int filter, FullSearchPackage searchPackage) {
        loadBeerTask.cancel();
        List<FilterBeerField> fieldList = Paper.book().read("beerCategoryList");
        FilterBeerPackage filterBeerPackage = new FilterBeerPackage();
        filterBeerPackage.setBeerCountries(fieldList.get(FilterBeerField.COUNTRY).getSelectedItemId());
        filterBeerPackage.setBeerTypes(fieldList.get(FilterBeerField.TYPE).getSelectedItemId());
        filterBeerPackage.setBeerStrengthes(fieldList.get(FilterBeerField.POWER).getSelectedItemId());
        filterBeerPackage.setBeerPacks(fieldList.get(FilterBeerField.BEER_PACK).getSelectedItemId());
        filterBeerPackage.setBeerBreweries(fieldList.get(FilterBeerField.BREWERY).getSelectedItemId());
        filterBeerPackage.setPage(searchPackage.getPage());
        filterBeerPackage.setCraft(craftBeer);
        filterBeerPackage.setBeerFiltered(filter);
        filterBeerPackage.setBeerDensity(fieldList.get(FilterBeerField.DENSITY).getSelectedItemId());
//        filterBeerPackage.setBeerFiltered(fieldList.get(FilterBeerField.COUNTRY).getSelectedItemId());

        filterBeerPackage.setBeerAveragepriceRange(fieldList.get(FilterBeerField.PRICE_BEER).getSelectedItemId());
        filterBeerPackage.setBeerColors(fieldList.get(FilterBeerField.COLOR).getSelectedItemId());
        filterBeerPackage.setBeerFragrances(fieldList.get(FilterBeerField.SMELL).getSelectedItemId());
        filterBeerPackage.setBeerTastes(fieldList.get(FilterBeerField.TASTE).getSelectedItemId());
        filterBeerPackage.setBeerAftertastes(fieldList.get(FilterBeerField.AFTER_TASTE).getSelectedItemId());
        filterBeerPackage.setBeerAftertastes(fieldList.get(FilterBeerField.BREWERY).getSelectedItemId());
        filterBeerPackage.setBeerIBU(fieldList.get(FilterBeerField.IBU).getSelectedItemId());
        loadBeerTask.execute(filterBeerPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.hideProgressBar();
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> restoLocations) {
                view.hideProgressBar();
                if (restoLocations.size() == 0) {
                    view.showMessage("Не найдено совпадений",0);
                } else {
                    view.appendItems(restoLocations);
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                view.hideProgressBar();
            }
        });
    }

    @Override
    public void loadBrewery(FullSearchPackage searchPackage) {
        loadBreweryTask.cancel();
        List<FilterBreweryField> fieldList = Paper.book().read("breweryCategoryList");
        BreweryPackage breweryPackage = new BreweryPackage();
        breweryPackage.setCountryId(fieldList.get(FilterBreweryField.COUNTRY).getSelectedItemId());
        breweryPackage.setBeerBrandId(fieldList.get(FilterBreweryField.BRAND).getSelectedItemId());
        breweryPackage.setBeerTypeId(fieldList.get(FilterBreweryField.TYPE_BEER).getSelectedItemId());
        breweryPackage.setPage(searchPackage.getPage());
        loadBreweryTask.execute(breweryPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.hideProgressBar();
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> restoLocations) {
                view.hideProgressBar();
                if (restoLocations.size() == 0) {
                    view.showMessage("Не найдено ни одной пивоварни",0);
                } else {
                    view.appendItems(restoLocations);
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                view.hideProgressBar();
            }
        });
    }
}
