package ru.frosteye.beermap.execution.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.base.ApiException;
import ru.frosteye.ovsa.execution.task.ObservableTask;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by oleg on 26.07.17.
 */

public abstract class BaseNetworkTask<P, R> extends ObservableTask<P, R> {

    private Api api;
    private Gson gson = new Gson();

    public BaseNetworkTask(MainThread mainThread,
                           Executor executor, Api api) {
        super(mainThread, executor);
        this.api = api;
    }

    protected Api getApi() {
        return api;
    }

    protected <Res> Res executeCall(Call<Res> call) {
        try {
            Response<Res> resResponse = call.execute();
            if(resResponse.isSuccessful()) {
                return resResponse.body();
            } else {
                String body = resResponse.errorBody().string();
                if(resResponse.code() == 409) {
                    throw new ApiException(gson.fromJson(body, MessageResponse.class).getMessage(), resResponse.code());
                } else {
                    throw new ApiException(((List<String>) gson.fromJson(body, new TypeToken<List<String>>() {
                    }.getType())).get(0), resResponse.code());
                }
            }
        } catch (IOException e) {
            throw new ApiException(e.getMessage(), 0);
        }
    }
}
