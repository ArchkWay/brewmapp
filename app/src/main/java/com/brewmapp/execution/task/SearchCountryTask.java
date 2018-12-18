package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Country;
import com.brewmapp.data.entity.CountryTypes;
import com.brewmapp.data.entity.wrapper.CountryInfo;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by nixus on 02.12.2017.
 */

public class SearchCountryTask extends BaseNetworkTask<GeoPackage, List<IFlexible>> {

    @Inject
    public SearchCountryTask(MainThread mainThread,
                             Executor executor,
                             Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(GeoPackage geoPackage) {
        return Observable.create(subscriber -> {
            try {

                String searchString = geoPackage.getCityName().toLowerCase();
                CountryTypes response = null;

                RequestParams requestParams = new RequestParams();
                requestParams.addParam("show_use_beer", 1);
                String key=getClass().getSimpleName();
                //List<IFlexible> flexibleList= Paper.book().read(key);
                //if(flexibleList==null){
                    response = executeCall(getApi().loadCountries(requestParams));
                    List<IFlexible> flexibleList=new ArrayList<>();
                  //  Paper.book().write(key,flexibleList);
                //}

                for(CountryInfo item : response.getModels()){
                    if(item.getModel().getName().toLowerCase().contains(searchString)){
                        flexibleList.add(item);
                    }
                }

                subscriber.onNext(flexibleList);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
