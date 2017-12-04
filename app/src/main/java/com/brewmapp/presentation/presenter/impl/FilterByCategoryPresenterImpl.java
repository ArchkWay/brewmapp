package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.data.pojo.PriceRangeType;
import com.brewmapp.data.pojo.ScrollPackage;
import com.brewmapp.execution.task.BeerAftertasteTask;
import com.brewmapp.execution.task.BeerBrandTask;
import com.brewmapp.execution.task.BeerColorTask;
import com.brewmapp.execution.task.BeerDensityTask;
import com.brewmapp.execution.task.BeerIbuTask;
import com.brewmapp.execution.task.BeerPackTask;
import com.brewmapp.execution.task.BeerPowerTask;
import com.brewmapp.execution.task.BeerSmellTask;
import com.brewmapp.execution.task.BeerTasteTask;
import com.brewmapp.execution.task.BeerTypesTask;
import com.brewmapp.execution.task.CountryTask;
import com.brewmapp.execution.task.FeatureTask;
import com.brewmapp.execution.task.FullSearchFilterTask;
import com.brewmapp.execution.task.KitchenTask;
import com.brewmapp.execution.task.LoadCityTaskFilter;
import com.brewmapp.execution.task.PriceRangeTask;
import com.brewmapp.execution.task.RegionTask;
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
    private CountryTask countryTask;
    private RegionTask regionTask;
    private LoadCityTaskFilter cityTask;

    //Beer filter queries
    private BeerTypesTask beerTypesTask;
    private BeerPackTask beerPackTask;
    private BeerBrandTask beerBrandTask;
    private BeerColorTask beerColorTask;
    private BeerTasteTask beerTasteTask;
    private BeerSmellTask beerSmellTask;
    private BeerAftertasteTask beerAftertasteTask;
    private BeerPowerTask beerPowerTask;
    private BeerDensityTask beerDensityTask;
    private BeerIbuTask beerIbuTask;

    @Inject
    public FilterByCategoryPresenterImpl(Context context, RestoTypeTask restoTypeTask,
                                         KitchenTask kitchenTask,
                                         PriceRangeTask priceRangeTask,
                                         FeatureTask featureTask,
                                         FullSearchFilterTask fullSearchFilterTask,
                                         BeerTypesTask beerTypesTask,
                                         BeerPackTask beerPackTask,
                                         BeerBrandTask beerBrandTask,
                                         BeerColorTask beerColorTask,
                                         BeerTasteTask beerTasteTask,
                                         BeerSmellTask beerSmellTask,
                                         BeerAftertasteTask beerAftertasteTask,
                                         BeerPowerTask beerPowerTask,
                                         BeerDensityTask beerDensityTask,
                                         BeerIbuTask beerIbuTask,
                                         CountryTask countryTask,
                                         RegionTask regionTask,
                                         LoadCityTaskFilter cityTask) {
        this.context = context;
        this.restoTypeTask = restoTypeTask;
        this.kitchenTask = kitchenTask;
        this.priceRangeTask = priceRangeTask;
        this.featureTask = featureTask;
        this.fullSearchFilterTask = fullSearchFilterTask;
        this.beerTypesTask = beerTypesTask;
        this.beerPackTask = beerPackTask;
        this.beerBrandTask = beerBrandTask;
        this.beerColorTask = beerColorTask;
        this.beerTasteTask = beerTasteTask;
        this.beerSmellTask = beerSmellTask;
        this.beerAftertasteTask = beerAftertasteTask;
        this.beerPowerTask = beerPowerTask;
        this.beerDensityTask = beerDensityTask;
        this.beerIbuTask = beerIbuTask;
        this.countryTask = countryTask;
        this.regionTask = regionTask;
        this.cityTask = cityTask;
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
        beerColorTask.cancel();
        beerTasteTask.cancel();
        beerSmellTask.cancel();
        beerAftertasteTask.cancel();
        beerPowerTask.cancel();
        beerDensityTask.cancel();
        beerIbuTask.cancel();
        countryTask.cancel();
        regionTask.cancel();
        cityTask.cancel();
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
                view.showProgressBar(false);
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
                view.showProgressBar(false);
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
                view.showProgressBar(false);
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
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void sendQueryFullSearch(FullSearchPackage fullSearchPackage) {
        fullSearchFilterTask.cancel();
        fullSearchFilterTask.execute(fullSearchPackage, new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showProgressBar(false);
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
                view.showProgressBar(false);
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
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerBrand(FullSearchPackage fullSearchPackage) {
        beerBrandTask.cancel();
        beerBrandTask.execute(fullSearchPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerColor() {
        beerColorTask.cancel();
        beerColorTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerTaste() {
        beerTasteTask.cancel();
        beerTasteTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerSmell() {
        beerSmellTask.cancel();
        beerSmellTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerAfterTaste() {
        beerAftertasteTask.cancel();
        beerAftertasteTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerPower() {
        beerPowerTask.cancel();
        beerPowerTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerDensity() {
        beerDensityTask.cancel();
        beerDensityTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadBeerIbu() {
        beerIbuTask.cancel();
        beerIbuTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadCountries() {
        countryTask.cancel();
        countryTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadRegions(GeoPackage geoPackage) {
        regionTask.cancel();
        regionTask.execute(geoPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }

    @Override
    public void loadCity(GeoPackage geoPackage) {
        cityTask.cancel();
        cityTask.execute(geoPackage, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                view.showProgressBar(false);
                showError(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                view.showProgressBar(false);
                view.appendItems(iFlexibles);
            }
        });
    }
}