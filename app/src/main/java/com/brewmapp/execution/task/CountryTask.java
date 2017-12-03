package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Country;
import com.brewmapp.data.entity.CountryTypes;
import com.brewmapp.data.entity.wrapper.CountryInfo;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by nixus on 02.12.2017.
 */

public class CountryTask extends BaseNetworkTask<Country, List<IFlexible>> {

    @Inject
    public CountryTask(MainThread mainThread,
                       Executor executor,
                       Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(Country country) {
        return Observable.create(subscriber -> {
            try {
                RequestParams requestParams = new RequestParams();
                requestParams.addParam("show_use_beer", 1);
                CountryTypes response = executeCall(getApi().loadCountries(requestParams));
                List<CountryInfo> countryInfos = new ArrayList<>();
                countryInfos.add(0, new CountryInfo(new Country("Любой город  ")));
                countryInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
