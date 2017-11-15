package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.util.Log;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.task.FeatureTask;
import com.brewmapp.execution.task.FullSearchTask;
import com.brewmapp.execution.task.KitchenTask;
import com.brewmapp.execution.task.PriceRangeTask;
import com.brewmapp.execution.task.RestoTypeTask;
import com.brewmapp.presentation.presenter.contract.FilterByCategoryPresenter;
import com.brewmapp.presentation.view.contract.FilterByCategoryView;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by nixus on 01.11.2017.
 */

public class FilterByCategoryPresenterImpl extends BasePresenter<FilterByCategoryView> implements FilterByCategoryPresenter {

    private Context context;
    private RestoTypeTask restoTypeTask;
    private KitchenTask kitchenTask;
    private PriceRangeTask priceRangeTask;
    private FeatureTask featureTask;
    private FullSearchTask fullSearchTask;

    @Inject
    public FilterByCategoryPresenterImpl(Context context, RestoTypeTask restoTypeTask,
                                         KitchenTask kitchenTask,
                                         PriceRangeTask priceRangeTask,
                                         FeatureTask featureTask,
                                         FullSearchTask fullSearchTask) {
        this.context = context;
        this.restoTypeTask = restoTypeTask;
        this.kitchenTask = kitchenTask;
        this.priceRangeTask = priceRangeTask;
        this.featureTask = featureTask;
        this.fullSearchTask = fullSearchTask;
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
        fullSearchTask.cancel();
    }

    @Override
    public void loadRestoTypes() {
        restoTypeTask.cancel();
        restoTypeTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
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
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadPriceRangeTypes() {
        priceRangeTask.cancel();
        priceRangeTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
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
        fullSearchTask.cancel();
        fullSearchTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                view.appendItems(iFlexibles);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(e.getMessage(),0);
            }
        });
    }
}