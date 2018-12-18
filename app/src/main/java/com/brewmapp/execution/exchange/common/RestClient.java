package com.brewmapp.execution.exchange.common;

import com.brewmapp.data.entity.Models;
import com.brewmapp.data.entity.wrapper.BeerAftertasteInfo;
import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.data.entity.wrapper.BeerColorInfo;
import com.brewmapp.data.entity.wrapper.BeerDensityInfo;
import com.brewmapp.data.entity.wrapper.BeerIbuInfo;
import com.brewmapp.data.entity.wrapper.BeerPackInfo;
import com.brewmapp.data.entity.wrapper.BeerPowerInfo;
import com.brewmapp.data.entity.wrapper.BeerSmellInfo;
import com.brewmapp.data.entity.wrapper.BeerTasteInfo;
import com.brewmapp.data.entity.wrapper.BeerTypeInfo;
import com.brewmapp.data.entity.wrapper.BreweryInfo;
import com.brewmapp.data.entity.wrapper.BreweryInfoSelect;
import com.brewmapp.data.entity.wrapper.CityInfo;
import com.brewmapp.data.entity.wrapper.CountryInfo;
import com.brewmapp.data.entity.wrapper.EventInfo;
import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.data.entity.wrapper.FilterBeerInfo;
import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.InterestInfoByUsers;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.data.entity.wrapper.RegionInfo;
import com.brewmapp.data.entity.wrapper.RestoDetailInfo;
import com.brewmapp.data.entity.wrapper.SearchRestoInfo;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
import com.brewmapp.data.entity.wrapper.ReviewInfo;
import com.brewmapp.data.entity.wrapper.SaleInfo;
import com.brewmapp.data.entity.wrapper.SearchBeerInfo;
import com.brewmapp.data.entity.wrapper.SubscriptionInfo;
import com.brewmapp.execution.exchange.common.base.BeerAftertasteDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerBrandDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerColorDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerDensityDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerIbuDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerPackDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerPowerDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerSmellDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerTasteDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerTypesDeserializer;
import com.brewmapp.execution.exchange.common.base.BreweryDeserializer;
import com.brewmapp.execution.exchange.common.base.BrewerySelectDeserializer;
import com.brewmapp.execution.exchange.common.base.CityDeserializer;
import com.brewmapp.execution.exchange.common.base.CountryDeserializer;
import com.brewmapp.data.entity.wrapper.UserInfo;
import com.brewmapp.execution.exchange.common.base.EventsDeserializer;
import com.brewmapp.execution.exchange.common.base.FeatureDeserializer;
import com.brewmapp.execution.exchange.common.base.FilterBeerDeserializer;
import com.brewmapp.execution.exchange.common.base.FilterRestoOnMapDeserializer;
import com.brewmapp.execution.exchange.common.base.InterestByUsersDeserializer;
import com.brewmapp.execution.exchange.common.base.InterestDeserializer;
import com.brewmapp.execution.exchange.common.base.BeerDeserializer;
import com.brewmapp.execution.exchange.common.base.KitchenDeserializer;
import com.brewmapp.execution.exchange.common.base.PriceRangeDeserializer;
import com.brewmapp.execution.exchange.common.base.QuickSearchDeserializer;
import com.brewmapp.execution.exchange.common.base.RegionDeserializer;
import com.brewmapp.execution.exchange.common.base.SearchRestoDeserializer;
import com.brewmapp.execution.exchange.common.base.RestoDetailsDeserializer;
import com.brewmapp.execution.exchange.common.base.ReviewDeserializer;
import com.brewmapp.execution.exchange.common.base.RestoTypeDeserializer;
import com.brewmapp.execution.exchange.common.base.SalesDeserializer;
import com.brewmapp.execution.exchange.common.base.SearchBeerDeserializer;
import com.brewmapp.execution.exchange.common.base.SearchRestoDeserializer2;
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
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;

import static com.brewmapp.BuildConfig.SERVER_API_VER;

/**
 * Created by oleg on 15.07.17.
 */

public class RestClient extends BaseRetrofitClient<Api> implements ApiClient {

    private boolean tokenOff=false;

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
                //.registerTypeAdapter(SearchRestoInfo.class, new SearchRestoDeserializer())
                .registerTypeAdapter(SearchRestoInfo.class, new SearchRestoDeserializer2())
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
                .registerTypeAdapter(BeerTypeInfo.class, new BeerTypesDeserializer())
                .registerTypeAdapter(BeerPackInfo.class, new BeerPackDeserializer())
                .registerTypeAdapter(BeerBrandInfo.class, new BeerBrandDeserializer())
                .registerTypeAdapter(BeerColorInfo.class, new BeerColorDeserializer())
                .registerTypeAdapter(BeerTasteInfo.class, new BeerTasteDeserializer())
                .registerTypeAdapter(BeerSmellInfo.class, new BeerSmellDeserializer())
                .registerTypeAdapter(BeerAftertasteInfo.class, new BeerAftertasteDeserializer())
                .registerTypeAdapter(BeerPowerInfo.class, new BeerPowerDeserializer())
                .registerTypeAdapter(BeerDensityInfo.class, new BeerDensityDeserializer())
                .registerTypeAdapter(BeerIbuInfo.class, new BeerIbuDeserializer())
                .registerTypeAdapter(CountryInfo.class, new CountryDeserializer())
                .registerTypeAdapter(RegionInfo.class, new RegionDeserializer())
                .registerTypeAdapter(CityInfo.class, new CityDeserializer())
                .registerTypeAdapter(FilterRestoLocationInfo.class, new FilterRestoOnMapDeserializer())
                .registerTypeAdapter(UserInfo.class, new UserDeserializer())
                .registerTypeAdapter(BreweryInfoSelect.class, new BrewerySelectDeserializer())
                .registerTypeAdapter(SearchBeerInfo.class, new SearchBeerDeserializer())
                .registerTypeAdapter(BreweryInfo.class, new BreweryDeserializer())
                ;
    }

    @Override
    public Class<Api> apiClass() {
        return Api.class;
    }

    @Override
    protected String getToken() {
        String token=null;
        if(tokenOff) {
            tokenOff=false;
        }else {
            token=super.getToken();
        }

        return token;
    }

    @Override
    public void setTokenOffTemporarily() {
        tokenOff=true;
    }
}
