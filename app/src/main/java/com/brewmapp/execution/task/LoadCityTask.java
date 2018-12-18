package com.brewmapp.execution.task;

import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.CountryTypes;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

public class LoadCityTask extends BaseNetworkTask<GeoPackage, List<City>> {

    @Inject
    public LoadCityTask(MainThread mainThread,
                        Executor executor,
                        Api api ) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<City>> prepareObservable(GeoPackage geoPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.CITY);
                params.addParam(Keys.NAME, geoPackage.getCityName() != null ? geoPackage.getCityName() : "");
                params.addParam(Keys.COUNTRY_ID, geoPackage.getCountryId() != null ? geoPackage.getCountryId() : "");
                params.addParam(Keys.REGION_ID, geoPackage.getRegionId() != null ? geoPackage.getRegionId() : "");

//                String key=getClass().getSimpleName();
//                List<IFlexible> flexibleList= Paper.book().read(key);
//                if(flexibleList==null){
//                    CountryTypes response = executeCall(getApi().loadCountries(requestParams));
//                    flexibleList=new ArrayList<>(response.getModels());
//                    Paper.book().write(key,flexibleList);
//                }

                ListResponse<City> response = executeCall(getApi().loadCity(params));
                subscriber.onNext(response.getModels());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
