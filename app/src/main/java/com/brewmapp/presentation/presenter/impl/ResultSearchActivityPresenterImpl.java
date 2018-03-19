package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.pojo.FilterBeerPackage;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.task.ApiBreweryTask;
import com.brewmapp.execution.task.FilterBeerTask;
import com.brewmapp.execution.task.FilterRestoTask;
import com.brewmapp.execution.task.RestosSearchTask;
import com.brewmapp.execution.task.SearchBeerTask;
import com.brewmapp.execution.task.SearchBreweryTask;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.ResultSearchActivityPresenter;
import com.brewmapp.presentation.view.contract.ResultSearchActivityView;

import java.util.List;

public class ResultSearchActivityPresenterImpl extends BasePresenter<ResultSearchActivityView> implements ResultSearchActivityPresenter {

    private RestosSearchTask loadRestosList;
    private SearchBeerTask loadBeerTask;
    private SearchBreweryTask loadBreweryTask;
    private ApiBreweryTask apiBreweryTask;
    private FilterRestoTask filterRestoTask;
    private FilterBeerTask filterBeerTask;


    @Inject
    public ResultSearchActivityPresenterImpl( RestosSearchTask loadRestosList,
                                             SearchBeerTask loadBeerTask,
                                              SearchBreweryTask loadBreweryTask,
                                             ApiBreweryTask apiBreweryTask,
                                              FilterRestoTask filterRestoTask,
                                              FilterBeerTask filterBeerTask
                                              ) {
        this.loadRestosList = loadRestosList;
        this.loadBeerTask = loadBeerTask;
        this.loadBreweryTask = loadBreweryTask;
        this.apiBreweryTask = apiBreweryTask;
        this.filterRestoTask = filterRestoTask;
        this.filterBeerTask = filterBeerTask;


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
        if(searchPackage.getLat()!=0)
            filterRestoPackage.setLat(searchPackage.getLat());
        if(searchPackage.getLon()!=0)
            filterRestoPackage.setLon(searchPackage.getLon());

        loadRestosList.execute(filterRestoPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                showError(e.getMessage());
                view.hideProgressBar();
                view.commonError(e.getMessage());
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
    }

    @Override
    public void getLocationsResto(FullSearchPackage searchPackage) {
        FilterRestoPackage restoPackage=new FilterRestoPackage();
        restoPackage.setRestoCity(searchPackage.getCity());
        filterRestoTask.execute(restoPackage,new SimpleSubscriber<List<FilterRestoLocation>>(){
            @Override
            public void onNext(List<FilterRestoLocation> filterRestoLocations) {
                super.onNext(filterRestoLocations);
                view.setLocations(filterRestoLocations);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }

    @Override
    public void getLocationsBeer(FullSearchPackage searchPackage) {
        FilterBeerPackage filterBeerPackage=new FilterBeerPackage();
        filterBeerPackage.setBeerCity(searchPackage.getCity());
        filterBeerTask.execute(filterBeerPackage,new SimpleSubscriber<List<FilterRestoLocation>>(){
            @Override
            public void onNext(List<FilterRestoLocation> filterRestoLocations) {
                super.onNext(filterRestoLocations);
                view.setLocations(filterRestoLocations);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }

        });
    }
}
