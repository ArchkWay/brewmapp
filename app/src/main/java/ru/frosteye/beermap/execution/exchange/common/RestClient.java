package ru.frosteye.beermap.execution.exchange.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import javax.inject.Inject;

import ru.frosteye.beermap.app.di.qualifier.ApiUrl;
import ru.frosteye.beermap.data.entity.wrapper.AlbumInfo;
import ru.frosteye.beermap.data.entity.wrapper.ContactInfo;
import ru.frosteye.beermap.data.entity.wrapper.PhotoInfo;
import ru.frosteye.beermap.data.entity.wrapper.PostInfo;
import ru.frosteye.beermap.execution.exchange.common.base.AlbumsDeserializer;
import ru.frosteye.beermap.execution.exchange.common.base.FriendsDeserializer;
import ru.frosteye.beermap.execution.exchange.common.base.PhotoDeserializer;
import ru.frosteye.beermap.execution.exchange.common.base.PostDeserializer;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.ovsa.execution.network.client.BaseRetrofitClient;
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;

/**
 * Created by oleg on 25.07.17.
 */

public class RestClient extends BaseRetrofitClient<Api> implements ApiClient {

    public static final String API_VERSION = "1.01";

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
    protected Map<String, String> getHeaders() {
        Map<String, String> map = super.getHeaders();
        map.put(Keys.API_VERSION, API_VERSION);

        return map;
    }

    @Override
    protected GsonBuilder createGsonBuilder() {
        return super.createGsonBuilder()
                .registerTypeAdapter(AlbumInfo.class, new AlbumsDeserializer())
                .registerTypeAdapter(PostInfo.class, new PostDeserializer())
                .registerTypeAdapter(ContactInfo.class, new FriendsDeserializer())
                .registerTypeAdapter(PhotoInfo.class, new PhotoDeserializer());
    }

    @Override
    public Class<Api> apiClass() {
        return Api.class;
    }
}
