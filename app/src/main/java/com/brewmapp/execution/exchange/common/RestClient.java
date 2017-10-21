package com.brewmapp.execution.exchange.common;

import com.brewmapp.data.entity.container.Products;
import com.brewmapp.data.entity.wrapper.EventInfo;
import com.brewmapp.data.entity.wrapper.ProductInfo;
import com.brewmapp.data.entity.wrapper.SaleInfo;
import com.brewmapp.execution.exchange.common.base.EventsDeserializer;
import com.brewmapp.execution.exchange.common.base.ProductsDeserializer;
import com.brewmapp.execution.exchange.common.base.SalesDeserializer;
import com.google.gson.GsonBuilder;

import java.util.Map;

import javax.inject.Inject;

import com.brewmapp.app.di.qualifier.ApiUrl;
import com.brewmapp.data.entity.wrapper.AlbumInfo;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.data.entity.wrapper.PhotoInfo;
import com.brewmapp.data.entity.wrapper.PostInfo;
import com.brewmapp.execution.exchange.common.base.AlbumsDeserializer;
import com.brewmapp.execution.exchange.common.base.FriendsDeserializer;
import com.brewmapp.execution.exchange.common.base.PhotoDeserializer;
import com.brewmapp.execution.exchange.common.base.PostDeserializer;
import com.brewmapp.execution.exchange.request.base.Keys;
import ru.frosteye.ovsa.execution.network.client.BaseRetrofitClient;
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;

/**
 * Created by oleg on 15.07.17.
 */

public class RestClient extends BaseRetrofitClient<Api> implements ApiClient {

    public static final String API_VERSION = "1.03";

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
                .registerTypeAdapter(EventInfo.class, new EventsDeserializer())
                .registerTypeAdapter(ContactInfo.class, new FriendsDeserializer())
                .registerTypeAdapter(SaleInfo.class, new SalesDeserializer())
                .registerTypeAdapter(PhotoInfo.class, new PhotoDeserializer())
                .registerTypeAdapter(ProductInfo.class, new ProductsDeserializer());
    }

    @Override
    public Class<Api> apiClass() {
        return Api.class;
    }
}
