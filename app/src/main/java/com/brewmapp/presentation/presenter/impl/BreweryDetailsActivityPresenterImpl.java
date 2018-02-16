package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.wrapper.BreweryInfo;
import com.brewmapp.data.pojo.ApiBreweryPackage;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.task.ApiBreweryTask;
import com.brewmapp.presentation.presenter.contract.BreweryDetailsActivityPresenter;
import com.brewmapp.presentation.view.contract.BreweryDetailsActivityView;

import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 04.02.2018.
 */

public class BreweryDetailsActivityPresenterImpl extends BasePresenter<BreweryDetailsActivityView> implements BreweryDetailsActivityPresenter {

    private ApiBreweryTask apiBreweryTask;
    private Brewery brewery;
    private Context context;

    @Inject
    public BreweryDetailsActivityPresenterImpl(ApiBreweryTask apiBreweryTask) {
        this.apiBreweryTask = apiBreweryTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void parseIntent(Intent intent) {
        try {
            String brewery_id=intent.getStringExtra(Actions.PARAM1);
            if(brewery_id==null)
                view.commonError();
            else
                requestContent(brewery_id);
        }catch (Exception e){
            view.commonError(e.getMessage());
        }
    }
//***********************************************
    private void requestContent(String brewery_id) {
        FullSearchPackage fullSearchPackage=new FullSearchPackage();
        fullSearchPackage.setId(brewery_id);
        apiBreweryTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                if(iFlexibles.size()==1) {
                    brewery = ((BreweryInfo) iFlexibles.get(0)).getModel();
                    view.setContent(brewery);
                }else
                    view.commonError(context.getString(R.string.replay_not_valid));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });
    }
}
