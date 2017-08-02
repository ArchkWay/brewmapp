package ru.frosteye.beermap.execution.exchange.common;

import javax.inject.Inject;

import ru.frosteye.beermap.app.di.qualifier.ApiUrl;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.ovsa.execution.network.client.BaseRetrofitClient;
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;

/**
 * Created by oleg on 25.07.17.
 */

public class RestClient extends BaseRetrofitClient<Api> implements ApiClient {

    @Inject
    public RestClient(@ApiUrl String baseUrl,
                      IdentityProvider identityProvider) {
        super(baseUrl, identityProvider);
    }

    @Override
    protected String getAuthHeaderName() {
        return Keys.TOKEN;
    }



    @Override
    public Class<Api> apiClass() {
        return Api.class;
    }
}
