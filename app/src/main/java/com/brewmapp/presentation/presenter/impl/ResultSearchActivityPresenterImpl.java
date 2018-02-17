package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import javax.inject.Inject;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.pojo.ApiBreweryPackage;
import com.brewmapp.data.pojo.FilterBeerPackage;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.task.ApiBreweryTask;
import com.brewmapp.execution.task.RestosSearchTask;
import com.brewmapp.execution.task.SearchBeerTask;
import com.brewmapp.execution.task.SearchBreweryTask;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.ResultSearchActivityPresenter;
import com.brewmapp.presentation.view.contract.ResultSearchActivityView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

import java.util.List;

public class ResultSearchActivityPresenterImpl extends BasePresenter<ResultSearchActivityView> implements ResultSearchActivityPresenter {

    private RestosSearchTask loadRestosList;
    private SearchBeerTask loadBeerTask;
    private SearchBreweryTask loadBreweryTask;
    private ApiBreweryTask apiBreweryTask;


    @Inject
    public ResultSearchActivityPresenterImpl( RestosSearchTask loadRestosList,
                                             SearchBeerTask loadBeerTask, SearchBreweryTask loadBreweryTask,
                                             ApiBreweryTask apiBreweryTask) {
        this.loadRestosList = loadRestosList;
        this.loadBeerTask = loadBeerTask;
        this.loadBreweryTask = loadBreweryTask;
        this.apiBreweryTask = apiBreweryTask;


    }

    @Override
    public void onAttach(ResultSearchActivityView searchView) {
        super.onAttach(searchView);

    }

    @Override
    public void onDestroy() {
        loadBeerTask.cancel();
        loadRestosList.cancel();
        loadBreweryTask.cancel();
        apiBreweryTask.cancel();
    }

    @Override
    public void loadRestoList(int specialOffer, FullSearchPackage searchPackage) {
        loadRestosList.cancel();

        FilterRestoPackage filterRestoPackage = new FilterRestoPackage();
        filterRestoPackage.setPage(searchPackage.getPage());
        if(searchPackage.getCity()!=null)
            filterRestoPackage.setRestoCity(searchPackage.getCity());
        if(searchPackage.getType()!=null)
            filterRestoPackage.setRestoTypes(searchPackage.getType());
        if(searchPackage.getBeer()!=null)
            filterRestoPackage.setRestoBeer(searchPackage.getBeer());
        if(searchPackage.getKitchen()!=null)
            filterRestoPackage.setRestoKitchens(searchPackage.getKitchen());
        if(searchPackage.getPrice()!=null)
            filterRestoPackage.setRestoPrices(searchPackage.getPrice());

        loadRestosList.execute(filterRestoPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                showError(e.getMessage());
                view.hideProgressBar();
            }

            @Override
            public void onNext(List<IFlexible> restoLocations) {
                view.hideProgressBar();
                    view.appendItems(restoLocations);
            }

            @Override
            public void onComplete() {
                view.hideProgressBar();
                super.onComplete();
            }
        });
    }

    @Override
    public void loadBeerList(FullSearchPackage searchPackage) {
        loadBeerTask.cancel();
        loadBeerTask.execute(searchPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.hideProgressBar();
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> restoLocations) {
                view.hideProgressBar();
                view.appendItems(restoLocations);
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

        apiBreweryTask.execute(searchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onError(Throwable e) {
                view.hideProgressBar();
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> restoLocations) {
                view.hideProgressBar();
                    view.appendItems(restoLocations);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                view.hideProgressBar();
            }
        });

//        loadBreweryTask.cancel();
//        List<FilterBreweryField> fieldList = Paper.book().read("breweryCategoryList");
//        BreweryPackage breweryPackage = new BreweryPackage();
//        breweryPackage.setCountryId(fieldList.get(FilterBreweryField.COUNTRY).getSelectedItemId());
//        breweryPackage.setBeerBrandId(fieldList.get(FilterBreweryField.BRAND).getSelectedItemId());
//        breweryPackage.setBeerTypeId(fieldList.get(FilterBreweryField.TYPE_BEER).getSelectedItemId());
//        breweryPackage.setPage(searchPackage.getPage());
//        loadBreweryTask.execute(breweryPackage, new SimpleSubscriber<List<IFlexible>>() {
//            @Override
//            public void onError(Throwable e) {
//                view.hideProgressBar();
//                showError(e.getMessage());
//            }
//
//            @Override
//            public void onNext(List<IFlexible> restoLocations) {
//                view.hideProgressBar();
//                if (restoLocations.size() == 0) {
//                    view.showMessage("Не найдено ни одной пивоварни",0);
//                } else {
//                    view.appendItems(restoLocations);
//                }
//            }
//
//            @Override
//            public void onComplete() {
//                super.onComplete();
//                view.hideProgressBar();
//            }
//        });
    }
}
