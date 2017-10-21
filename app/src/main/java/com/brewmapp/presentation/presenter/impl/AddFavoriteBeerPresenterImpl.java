package com.brewmapp.presentation.presenter.impl;

import android.app.Activity;
import android.content.Context;

import com.brewmapp.data.pojo.LoadProductPackage;
import com.brewmapp.execution.task.LoadProductTask;
import com.brewmapp.presentation.presenter.contract.AddFavoriteBeerPresenter;
import com.brewmapp.presentation.view.contract.AddFavoriteBeerView;
import com.brewmapp.utils.Cons;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 10/20/2017.
 */

public class AddFavoriteBeerPresenterImpl extends BasePresenter<AddFavoriteBeerView> implements AddFavoriteBeerPresenter {

    private LoadProductTask loadProductTask;

    @Inject
    public AddFavoriteBeerPresenterImpl(LoadProductTask loadProductTask){
            this.loadProductTask = loadProductTask;
    }



    @Override
    public void onAttach(AddFavoriteBeerView addFavoriteBeerView) {
        super.onAttach(addFavoriteBeerView);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void sendQuery(LoadProductPackage loadProductPackage) {
        loadProductTask.cancel();
        if(loadProductPackage.getStringSearch().length()==0){
            view.appendItems(new ArrayList<IFlexible>());
            return;
        }

        loadProductTask.execute(loadProductPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> product) {
                super.onNext(product);
                view.appendItems(product);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

}
