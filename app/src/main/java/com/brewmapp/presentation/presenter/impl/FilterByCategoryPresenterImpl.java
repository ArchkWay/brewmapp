package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.pojo.BeerTypes;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.PriceRangeType;
import com.brewmapp.data.pojo.ScrollPackage;
import com.brewmapp.execution.task.BeerBrandTask;
import com.brewmapp.execution.task.BeerPackTask;
import com.brewmapp.execution.task.BeerTypesTask;
import com.brewmapp.execution.task.FeatureTask;
import com.brewmapp.execution.task.FullSearchFilterTask;
import com.brewmapp.execution.task.KitchenTask;
import com.brewmapp.execution.task.PriceRangeTask;
import com.brewmapp.execution.task.RestoTypeTask;
import com.brewmapp.presentation.presenter.contract.FilterByCategoryPresenter;
import com.brewmapp.presentation.view.contract.FilterByCategoryView;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by nixus on 01.11.2017.
 */

public class FilterByCategoryPresenterImpl extends BasePresenter<FilterByCategoryView> implements FilterByCategoryPresenter {

    private Context context;
    //Resto filter queries
    private RestoTypeTask restoTypeTask;
    private KitchenTask kitchenTask;
    private PriceRangeTask priceRangeTask;
    private FeatureTask featureTask;
    private FullSearchFilterTask fullSearchFilterTask;

    //Beer filter queries
    private BeerTypesTask beerTypesTask;
    private BeerPackTask beerPackTask;
    private BeerBrandTask beerBrandTask;

    @Inject
    public FilterByCategoryPresenterImpl(Context context, RestoTypeTask restoTypeTask,
                                         KitchenTask kitchenTask,
                                         PriceRangeTask priceRangeTask,
                                         FeatureTask featureTask,
                                         FullSearchFilterTask fullSearchFilterTask,
                                         BeerTypesTask beerTypesTask,
                                         BeerPackTask beerPackTask,
                                         BeerBrandTask beerBrandTask) {
        this.context = context;
        this.restoTypeTask = restoTypeTask;
        this.kitchenTask = kitchenTask;
        this.priceRangeTask = priceRangeTask;
        this.featureTask = featureTask;
        this.fullSearchFilterTask = fullSearchFilterTask;
        this.beerTypesTask = beerTypesTask;
        this.beerPackTask = beerPackTask;
        this.beerBrandTask = beerBrandTask;
    }

    @Override
    public void onAttach(FilterByCategoryView filterByCategoryView) {
        super.onAttach(filterByCategoryView);
    }

    @Override
    public void onDestroy() {
        restoTypeTask.cancel();
        kitchenTask.cancel();
        priceRangeTask.cancel();
        featureTask.cancel();
        fullSearchFilterTask.cancel();
        beerTypesTask.cancel();
        beerPackTask.cancel();
        beerBrandTask.cancel();
    }

    @Override
    public void loadRestoTypes() {
        restoTypeTask.cancel();
        restoTypeTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadKitchenTypes() {
        kitchenTask.cancel();
        kitchenTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadPriceRangeTypes(String type) {
        priceRangeTask.cancel();
        PriceRangeType priceRangeType = new PriceRangeType(type);
        priceRangeTask.execute(priceRangeType, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadFeatureTypes() {
        featureTask.cancel();
        featureTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void sendQueryFullSearch(FullSearchPackage fullSearchPackage) {
        fullSearchFilterTask.cancel();
        fullSearchFilterTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(e.getMessage(),0);
            }
        });
    }

    @Override
    public void loadBeerTypes() {
        beerTypesTask.cancel();
        beerTypesTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerPack() {
        beerPackTask.cancel();
        beerPackTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerBrand(ScrollPackage scrollPackage) {
        beerBrandTask.cancel();
        beerBrandTask.execute(scrollPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.appendItems(iFlexibles);
            }
        });
    }
}