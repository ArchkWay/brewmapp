package com.brewmapp.execution.exchange.common;

import com.brewmapp.data.entity.Feature;
import com.brewmapp.data.entity.Models;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.entity.wrapper.EventInfo;
import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.data.entity.wrapper.FilterBeerInfo;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.InterestInfoByUsers;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.data.entity.wrapper.RestoDetailInfo;
import com.brewmapp.data.entity.wrapper.RestoInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
import com.brewmapp.data.entity.wrapper.ReviewInfo;
import com.brewmapp.data.entity.wrapper.SaleInfo;
import com.brewmapp.data.entity.wrapper.SubscriptionInfo;
import com.brewmapp.data.entity.wrapper.UserInfo;
import com.brewmapp.execution.exchange.common.base.EventsDeserializer;
import com.brewmapp.execution.exchange.common.base.FeatureDeserializer;
import com.brewmapp.execution.exchange.common.base.FilterBeerDeserializer;
import com.brewmapp.execution.exchange.common.base.InterestByUsersDeserializer;
import com.brewmapp.execution.exchange.common.base.InterestDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerDeserializer;
import com.brewmapp.execution.exchange.common.base.KitchenDeserializer;
import com.brewmapp.execution.exchange.common.base.PriceRangeDeserializer;
import com.brewmapp.execution.exchange.common.base.QuickSearchDeserializer;
import com.brewmapp.execution.exchange.common.base.RestoDeserializer;
import com.brewmapp.execution.exchange.common.base.RestoDetailsDeserializer;
import com.brewmapp.execution.exchange.common.base.ReviewDeserializer;
import com.brewmapp.execution.exchange.common.base.RestoTypeDeserializer;
import com.brewmapp.execution.exchange.common.base.SalesDeserializer;
import com.brewmapp.execution.exchange.common.base.SubscriptionDeserializer;
import com.brewmapp.execution.exchange.common.base.UserDeserializer;
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

import static com.brewmapp.BuildConfig.SERVER_API_VER;

/**
 * Created by oleg on 15.07.17.
 */

public class RestClient extends BaseRetrofitClient<Api> implements ApiClient {

    //public static final String API_VERSION = "1.02";

    //public static final String API_VERSION = "1.03";

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
        map.put(Keys.API_VERSION, SERVER_API_VER);

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
                .registerTypeAdapter(BeerInfo.class, new BeerDeserializer())
                .registerTypeAdapter(InterestInfo.class, new InterestDeserializer())
                .registerTypeAdapter(RestoInfo.class, new RestoDeserializer())
                .registerTypeAdapter(RestoDetailInfo.class, new RestoDetailsDeserializer())
                .registerTypeAdapter(RestoTypeInfo.class, new RestoTypeDeserializer())
                .registerTypeAdapter(KitchenInfo.class, new KitchenDeserializer())
                .registerTypeAdapter(PriceRangeInfo.class, new PriceRangeDeserializer())
                .registerTypeAdapter(FeatureInfo.class, new FeatureDeserializer())
                .registerTypeAdapter(SubscriptionInfo.class, new SubscriptionDeserializer())
                .registerTypeAdapter(ReviewInfo.class, new ReviewDeserializer())
                .registerTypeAdapter(FilterBeerInfo.class, new FilterBeerDeserializer())
                .registerTypeAdapter(InterestInfoByUsers.class, new InterestByUsersDeserializer())
                .registerTypeAdapter(Models.class, new QuickSearchDeserializer())
                .registerTypeAdapter(UserInfo.class, new UserDeserializer())
                ;
    }

    @Override
    public Class<Api> apiClass() {
        return Api.class;
    }
}
