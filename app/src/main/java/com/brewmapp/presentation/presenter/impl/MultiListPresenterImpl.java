package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.FullSearchTask;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.execution.task.QuickSearchTask;
import com.brewmapp.presentation.presenter.contract.MultiListPresenter;
import com.brewmapp.presentation.view.contract.MultiListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 10/20/2017.
 */

public class MultiListPresenterImpl extends BasePresenter<MultiListView> implements MultiListPresenter {


    private FullSearchTask fullSearchTask;
    private QuickSearchTask quickSearchTask;



    @Inject
    public MultiListPresenterImpl(FullSearchTask fullSearchTask, QuickSearchTask quickSearchTask){
        this.fullSearchTask = fullSearchTask;
        this.quickSearchTask = quickSearchTask;

    }



    @Override
    public void onAttach(MultiListView multiListView) {
        super.onAttach(multiListView);
    }

    @Override
    public void onDestroy() {

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
                view.onError();
                view.showMessage(e.getMessage(),0);
            }
        });

    }

    @Override
    public int parseIntent(Intent intent) {
        switch (intent.getAction()) {
            case Keys.CAP_BEER:
                return MultiListView.MODE_ACTIVTY_SHOW_AND_SELECT_BEER;
            case Keys.CAP_RESTO:
                return MultiListView.MODE_ACTIVTY_SHOW_AND_SELECT_RESTO;
            case Keys.HASHTAG:
                return MultiListView.MODE_ACTIVTY_SHOW_HASHTAG;
            case Keys.CAP_USER_FRIENDS:
                return MultiListView.MODE_ACTIVTY_SHOW_AND_SELECT_FRIENDS;
            default:
                return MultiListView.MODE_ACTIVTY_ERROR;
        }
    }

    @Override
    public void sentQueryQuickSearch(FullSearchPackage fullSearchPackage) {
        quickSearchTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                view.appendItems(iFlexibles);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.appendItems(new ArrayList<IFlexible>());
            }
        });

    }

}
