package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.pojo.SearchPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.LocationQueryParams;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 30.09.17.
 */

public class SearchRestosTask extends SearchTask<ListResponse<Resto>> {

    private UserRepo userRepo;

    @Inject
    public SearchRestosTask(MainThread mainThread,
                            Executor executor,
                            Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Call<ListResponse<Resto>> prepareCall(RequestParams queryParams,
                                                    RequestParams fields) {
        queryParams.addParam(Keys.USER_ID, userRepo.load().getId());
        return getApi().findRestos(queryParams, fields);
    }
}
