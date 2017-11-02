package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.data.entity.RestoType;
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

    private RestoTypeTask restoTypeTask;

    @Inject
    public FilterByCategoryPresenterImpl(RestoTypeTask restoTypeTask) {
        this.restoTypeTask = restoTypeTask;
    }

    @Override
    public void onDestroy() {
        restoTypeTask.cancel();
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
}