package com.brewmapp.execution.exchange.common;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import com.brewmapp.data.entity.Album;
import com.brewmapp.data.entity.AverageEvaluation;
import com.brewmapp.data.entity.Averagevalue;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerAftertasteTypes;
import com.brewmapp.data.entity.BeerBrandTypes;
import com.brewmapp.data.entity.BeerColorTypes;
import com.brewmapp.data.entity.BeerDensityTypes;
import com.brewmapp.data.entity.BeerIbuTypes;
import com.brewmapp.data.entity.BeerLocation;
import com.brewmapp.data.entity.BeerPackTypes;
import com.brewmapp.data.entity.BeerPowerTypes;
import com.brewmapp.data.entity.BeerSmellTypes;
import com.brewmapp.data.entity.BeerTasteTypes;
import com.brewmapp.data.entity.BeerTypesModel;
import com.brewmapp.data.entity.BreweryTypes;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.CityTypes;
import com.brewmapp.data.entity.CountryTypes;
import com.brewmapp.data.entity.EvaluationBeer;
import com.brewmapp.data.entity.EvaluationResto;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.FeatureTypes;
import com.brewmapp.data.entity.KitchenTypes;
import com.brewmapp.data.entity.LocalizedStrings;
import com.brewmapp.data.entity.LocationChild;
import com.brewmapp.data.entity.MenuResto;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.PhotoDetails;
import com.brewmapp.data.entity.PriceRangeTypes;
import com.brewmapp.data.entity.RegionTypes;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.RestoTypes;
import com.brewmapp.data.entity.Sales;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.container.AlbumPhotos;
import com.brewmapp.data.entity.container.Albums;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.container.BeerBrands;
import com.brewmapp.data.entity.container.Breweries;
import com.brewmapp.data.entity.container.Events;
import com.brewmapp.data.entity.container.FilterBeer;
import com.brewmapp.data.entity.container.Interests;
import com.brewmapp.data.entity.container.InterestsByUser;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.container.Beers;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.data.entity.container.ResponseSearchResto;
import com.brewmapp.data.entity.container.Reviews;
import com.brewmapp.data.entity.container.SearchBeerTypes;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.entity.container.Users;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.WrapperValues;
import com.brewmapp.execution.exchange.response.QuickSearchResponse;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.exchange.response.UserResponse;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;

import java.util.Map;

import ru.frosteye.ovsa.execution.network.request.MultipartRequestParams;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 15.07.17.
 */

public interface Api {




    @POST("start/login")
    @FormUrlEncoded
    Call<UserResponse> login(@FieldMap WrapperParams params);

    @POST("fb/auth")
    @FormUrlEncoded
    Call<UserResponse> fbLogin(@FieldMap RequestParams params);


    @GET("start/code")
    Call<MessageResponse> requestCode(@QueryMap RequestParams params);

    @POST("user/user")
    @FormUrlEncoded
    Call<ListResponse<User>> getProfile(@FieldMap RequestParams params);

    @GET("start/CheckCode")
    Call<UserResponse> confirmCode(@QueryMap RequestParams params);

    @POST("start/avatar")
    @Multipart
    Call<MessageResponse> uploadAvatar(@PartMap MultipartRequestParams params);

    @POST("profile")
    @FormUrlEncoded
    Call<ListResponse<User>> createPassword(@FieldMap WrapperParams params);

    @POST("start/register")
    @FormUrlEncoded
    Call<UserResponse> register(@FieldMap WrapperParams params);

    @POST("friends")
    @FormUrlEncoded
    Call<ListResponse<ContactInfo>> listFriends(@FieldMap WrapperParams params);

    @POST("photoalbum")
    @FormUrlEncoded
    Call<Albums> loadUserAlbums(@FieldMap WrapperParams params);

    @POST("news")
    @FormUrlEncoded
    Call<Posts> loadPosts(@Query(Keys.LIMIT_START) int start,
                          @Query(Keys.LIMIT_END) int end,
                          @FieldMap WrapperParams params);

    @POST("subscription")
    @FormUrlEncoded
    Call<Subscriptions> loadUserSubscriptionsItems(@FieldMap WrapperParams params);


    @POST("beer/beer")
    @FormUrlEncoded
    Call<Beers> loadProduct(@Query(Keys.LIMIT_START) int start,
                            @Query(Keys.LIMIT_END) int end,
                            @FieldMap WrapperParams params);

    @POST("userinterest")
    @FormUrlEncoded
    Call<Interests> loadInterest(@Query(Keys.LIMIT_START) int start,
                                 @Query(Keys.LIMIT_END) int end,
                                 @FieldMap WrapperParams params);

    @POST("userinterest/add")
    @FormUrlEncoded
    Call<SingleResponse<Interest>> addInterest(@FieldMap WrapperParams params);

    @POST("reviews/add")
    @FormUrlEncoded
    Call<Object> addReview(@FieldMap WrapperParams params);

    @POST("subscription/add")
    @FormUrlEncoded
    Call<SingleResponse<Subscription>> addSubscription(@FieldMap WrapperParams params);

    @POST("subscription/delete")
    @FormUrlEncoded
    Call<Object> deleteSubscription(@FieldMap WrapperParams params);

    @POST("userinterest/delete")
    @FormUrlEncoded
    Call<Object> removeInterest(@FieldMap WrapperParams params);


    @POST("event")
    @FormUrlEncoded
    Call<Events> loadEvents(@Query(Keys.LIMIT_START) int start,
                            @Query(Keys.LIMIT_END) int end,
                            @FieldMap WrapperParams params);



    @POST("news")
    @FormUrlEncoded
    Call<Posts> loadNews(@FieldMap WrapperParams params);

    @POST("photoalbum/add")
    @FormUrlEncoded
    Call<SingleResponse<Album>> createAlbum(@FieldMap WrapperParams params);

    @POST("photoalbum/edit")
    @FormUrlEncoded
    Call<SingleResponse<Album>> editAlbum(@FieldMap WrapperParams params);

    @POST("news/add")
    @FormUrlEncoded
    Call<SingleResponse<Post>> createPost(@FieldMap WrapperParams params);

    @POST("news/delete")
    @FormUrlEncoded
    Call<SingleResponse<Post>> deletePost(@FieldMap WrapperParams params);

    @POST("photo/add")
    @Multipart
    Call<SingleResponse<UploadPhotoResponse>> uploadPhoto(@PartMap MultipartRequestParams params);

    @POST("photo")
    @FormUrlEncoded
    Call<AlbumPhotos> loadPhotosForAlbum(@FieldMap WrapperParams params);


    @GET("claim/types")
    Call<SingleResponse<Map<String, String>>> claimTypes();

    @POST("claim/add")
    @FormUrlEncoded
    Call<SingleResponse<Map<String, String>>> claim(@FieldMap WrapperParams params);

    @POST("like/add")
    @FormUrlEncoded
    Call<MessageResponse> likeDislike(@FieldMap WrapperParams params);

    @POST("photoalbum/delete")
    @FormUrlEncoded
    Call<MessageResponse> deleteAlbum(@FieldMap WrapperParams params);

    @POST("photo/delete")
    @FormUrlEncoded
    Call<MessageResponse> deletePhoto(@FieldMap WrapperParams params);

    @GET("quick_search/{query}")
    Call<MessageResponse> quickSearch(@Path("query") String query,
                                      @Query("lat") double lat, @Query("lon") double lon);

    @POST("search/resto")
    @FormUrlEncoded
    Call<ListResponse<Resto>> findRestos(@QueryMap RequestParams query,
                                         @FieldMap RequestParams params);

    @POST("search/resto")
    @FormUrlEncoded
    Call<ResponseSearchResto> searchResto(@QueryMap RequestParams query,
                                          @Query(Keys.LAT) double lat,
                                          @Query(Keys.LON) double lon,
                                          @Query(Keys.LIMIT_START) int start,
                                          @Query(Keys.LIMIT_END) int end,
                                          @FieldMap RequestParams params);

    @POST("search/beer")
    @FormUrlEncoded
    Call<SearchBeerTypes> loadBeers(@Query(Keys.LIMIT_START) int start,
                                    @Query(Keys.LIMIT_END) int end,
                                    @FieldMap RequestParams params);

    @POST("subscription")
    @FormUrlEncoded
    Call<ListResponse<Subscription>> loadUserSubscriptionsList(@FieldMap WrapperParams params);


    @POST("geo/location")
    @FormUrlEncoded
    Call<ListResponse<BeerLocation.LocationInfo>> loadLocationById(@FieldMap WrapperParams params);

    @GET("resto/allrestoincity")
    Call<ListResponse<FilterRestoLocation>> loadRestoLocationInCity(@Query(Keys.CITY_ID) int cityId);

    @POST("geo/city")
    @FormUrlEncoded
    Call<ListResponse<City>> loadCity(@FieldMap WrapperParams params);

    @POST("geo/city")
    @FormUrlEncoded
    Call<CityTypes> loadCityFilter(@FieldMap WrapperParams params);

    @POST("full_search/{query}")
    @FormUrlEncoded
    Call<Beers> fullSearchBeer(
            @Path("query") String query,
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap WrapperParams params

    );

    @POST("full_search/{query}")
    @FormUrlEncoded
    Call<ResponseSearchResto> fullSearchResto(
            @Path("query") String query,
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap WrapperParams params
    );

    @POST("full_search/{query}")
    @FormUrlEncoded
    Call<ResponseSearchResto> fullSearchRestoWithLocate(
            @Path("query") String query,
            @Query(Keys.LAT) double lat,
            @Query(Keys.LON) double lon,
            @FieldMap WrapperParams params
    );

    @POST("full_search/{query}")
    @FormUrlEncoded
    Call<Breweries> fullSearchBrewery(
            @Path("query") String query,
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap WrapperParams params
    );

    @POST("resto/restodata")
    @FormUrlEncoded
    Call<RestoDetails> getRestoDetails(@Query(Keys.RESTO_ID) String query, @FieldMap WrapperParams params);

    @POST("resto/restodata")
    @FormUrlEncoded
    Call<RestoDetails> getRestoDetailsWithDistance(
            @Query(Keys.RESTO_ID) String query,
            @FieldMap WrapperParams params,
            @Query(Keys.LAT) double start,
            @Query(Keys.LON) double end,
            @Query(Keys.NOMENU) double nomenu
        );

    @POST("resto/restodata")
    @FormUrlEncoded
    Call<ListResponse<RestoDetails>> getMultiRestoDetails(@Query(Keys.RESTO_ID) String query, @FieldMap WrapperParams params);

    @GET("resto/type")
    Call<RestoTypes> loadRestoTypes();

    @GET("resto/kitchen")
    Call<KitchenTypes> loadKitchenTypes();

    @GET("{type}/pricerange")
    Call<PriceRangeTypes> loadPriceRanges(@Path("type") String type);

    @GET("resto/feature")
    Call<FeatureTypes> loadFeature();

    @POST("shares")
    @FormUrlEncoded
    Call<Sales> loadSales(@Query(Keys.LIMIT_START) int start,
                          @Query(Keys.LIMIT_END) int end,
                          @FieldMap WrapperParams params);

    @POST("reviews")
    @FormUrlEncoded
    Call<Reviews> loadReviews(@FieldMap WrapperParams params);

    @POST("reviews")
    @FormUrlEncoded
    Call<Reviews> loadReviews(@Query(Keys.LIMIT_START) int start,
                              @Query(Keys.LIMIT_END) int end,
                              @FieldMap WrapperParams params);

    @POST("/api/resto/restoevaluation")
    @FormUrlEncoded
    Call<ListResponse<EvaluationResto>> getRestoEvaluation(@FieldMap WrapperParams params);

    @POST("/api/resto/restoevaluation")
    @FormUrlEncoded
    Call<ListResponse<EvaluationResto>> getRestoEvaluationByToken(@Header("token") String tok, @FieldMap WrapperParams params);

    @POST("/api/resto/restoaveragevalue")
    @FormUrlEncoded
    Call<ListResponse<AverageEvaluation>> getRestoAverageEvaluation(@FieldMap WrapperParams params);

    @POST("/api/resto/restoevaluation/add")
    @FormUrlEncoded
    Call<Object> setRestoEvaluation(@FieldMap WrapperParams params);

    @POST("full_search/{query}")
    @FormUrlEncoded
    Call<FilterBeer> filterSearchBeer(
            @Path("query") String query,
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap WrapperParams params

    );

    @POST("resto/getcoordinates")
    @FormUrlEncoded
    Call<ListResponse<FilterRestoLocation>> loadRestoLocation(@FieldMap RequestParams params);

    @POST("beer/getcoordinates")
    @FormUrlEncoded
    Call<ListResponse<FilterRestoLocation>> loadBeerLocation(@FieldMap RequestParams params);

    @POST("profile")
    @FormUrlEncoded
    Call<ListResponse<User>> profileEdit(@FieldMap WrapperParams params);

    @POST("resto/getcoordinates")
    @FormUrlEncoded
    Call<ListResponse<FilterRestoLocationInfo>> loadRestoGeo(@FieldMap WrapperValues params);

    @POST("/api/like")
    @FormUrlEncoded
    Call<Object>  loadLikesByBeer(@FieldMap WrapperParams params);

    @POST("userinterest")
    @FormUrlEncoded
    Call<InterestsByUser> loadInterestByUsers(@Query(Keys.LIMIT_START) int start,
                                       @Query(Keys.LIMIT_END) int end,
                                       @FieldMap WrapperParams params);

    @GET("quick_search/{query}")
    Call<QuickSearchResponse> quickSearch(@Path("query") String query, @Query("hashtagonly") int end);

    @POST("beer/type")
    @FormUrlEncoded
    Call<BeerTypesModel> loadBeerTypes(@Query(Keys.LIMIT_START) int start,
                                       @Query(Keys.LIMIT_END) int end,
                                       @FieldMap WrapperParams params);

    @POST("beer/packing")
    @FormUrlEncoded
    Call<BeerPackTypes> loadBeerPack(@FieldMap WrapperParams params);

    @POST("beer/brand")
    @FormUrlEncoded
    Call<BeerBrandTypes> loadBeerBrands(@FieldMap WrapperParams params);

    @POST("beer/color")
    @FormUrlEncoded
    Call<BeerColorTypes> loadBeerColors(@FieldMap WrapperParams params);

    @POST("beer/taste")
    @FormUrlEncoded
    Call<BeerTasteTypes> loadBeerTaste(@FieldMap WrapperParams params);

    @POST("beer/fragrance")
    @FormUrlEncoded
    Call<BeerSmellTypes> loadBeerSmell(@FieldMap WrapperParams params);

    @POST("beer/aftertaste")
    @FormUrlEncoded
    Call<BeerAftertasteTypes> loadBeerAfterTaste(@FieldMap WrapperParams params);

    @POST("beer/strength")
    @FormUrlEncoded
    Call<BeerPowerTypes> loadBeerPower(@FieldMap WrapperParams params);

    @POST("beer/density")
    @FormUrlEncoded
    Call<BeerDensityTypes> loadBeerDensity(@FieldMap WrapperParams params);

    @POST("brewery/shortdata")
    @FormUrlEncoded
    Call<BreweryTypes> loadBrewery(@Query(Keys.LIMIT_START) int start,
                                   @Query(Keys.LIMIT_END) int end,
                                    @FieldMap WrapperParams params);

    @GET("beer/ibu")
    Call<BeerIbuTypes> loadBeerIbu();

    @POST("geo/country")
    @FormUrlEncoded
    Call<CountryTypes> loadCountries(@FieldMap RequestParams requestParams);

    @POST("geo/region")
    @FormUrlEncoded
    Call<RegionTypes> loadRegions(@FieldMap WrapperParams params);

    @POST("resto/getcoordinatesbytext/{query}")
    @FormUrlEncoded
    Call<ListResponse<FilterRestoLocationInfo>> searchOnMap(
            @Path("query") String query,
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap RequestParams requestParams
    );

    @POST("full_search/{query}")
    @FormUrlEncoded
    Call<Users> fullSearchUser(
            @Path("query") String query,
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap WrapperParams params

    );

    @POST("friends/add")
    @FormUrlEncoded
    Call<Object> addFriend(@FieldMap WrapperParams params);

    @POST("friends/allow")
    @FormUrlEncoded
    Call<Object> allowFriend(@FieldMap WrapperParams params);

    @POST("friends/delete")
    @FormUrlEncoded
    Call<Object> deleteFriend(@FieldMap WrapperParams params);

    @POST("user/user")
    @FormUrlEncoded
    Call<Users> getUsers(@FieldMap RequestParams params);

    @POST("user/chkuser")
    @FormUrlEncoded
    Call<SingleResponse<User>> chkUser(@FieldMap RequestParams params);

    @POST("user/delete")
    @FormUrlEncoded
    Call<Object>  deleteUser(@FieldMap RequestParams params);

    @POST("beer/beer/edit")
    @FormUrlEncoded
    Call<SingleResponse<Beer>> editBeer(@FieldMap WrapperParams params);

    @POST("full_search/{query}")
    @FormUrlEncoded
    Call<BeerBrands> fullSearchBeerBrand(
            @Path("query") String query,
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap WrapperParams params

    );

    @POST("photo")
    @FormUrlEncoded
    Call<ListResponse<Photo>> loadPhotosResto(@FieldMap WrapperParams params);

    @POST("resto/resto/edit")
    @FormUrlEncoded
    Call<SingleResponse<RestoDetail>> editResto(@FieldMap WrapperParams params);

    @POST("geo/location/add")
    @FormUrlEncoded
    Call<SingleResponse<LocationChild>> createLocation(@FieldMap WrapperParams params);

    @POST("photo")
    @FormUrlEncoded
    Call<ListResponse<PhotoDetails>> getPhotos(@FieldMap WrapperParams params);


    @POST("userinterest/search")
    @FormUrlEncoded
    Call<InterestsByUser> loadUsersByInterest(@FieldMap RequestParams params);

    @POST("beer/productaveragevalue")
    @FormUrlEncoded
    Call<ListResponse<Averagevalue>> loadProductAverageValue(@FieldMap RequestParams params);

    @POST("/api/beer/productevaluation")
    @FormUrlEncoded
    Call<ListResponse<EvaluationBeer>> getBeerEvaluation(@FieldMap WrapperParams params);

    @POST("/api/beer/productevaluation/add")
    @FormUrlEncoded
    Call<Object> setBeerEvaluation(@FieldMap WrapperParams params);

    @POST("/api/brewery")
    @FormUrlEncoded
    Call<Breweries>  apiBrewery(
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap RequestParams params);

    @POST("/api/reviews/approval")
    @FormUrlEncoded
    Call<Object>  addReviewsApproval(@FieldMap RequestParams params);

    @POST("resto/menu")
    @FormUrlEncoded
    Call<ListResponse<MenuResto>> loadMenuResto(@Query(Keys.LIMIT_START) int start,
                                                @Query(Keys.LIMIT_END) int end,
                                                @FieldMap WrapperParams params);

    @POST("getactivety")
    @FormUrlEncoded
    Call<ListResponse<User>> getActivetyUsets(@FieldMap RequestParams params);

    @POST("resto/resto/add")
    @FormUrlEncoded
    Call<Object> createResto(@FieldMap RequestParams params);

    @POST("beer/beer/add")
    @FormUrlEncoded
    Call<Object> createBeer(@FieldMap RequestParams params);

    @POST("requestmoderation/relatedmodel")
    @FormUrlEncoded
    Call<SingleResponse<LocalizedStrings>> getModerationRelModels(@FieldMap WrapperParams params);

    @GET("reviews/relatedmodel")
    Call<SingleResponse<LocalizedStrings>> getReviewsRelModels();

    @GET("news/relatedmodel")
    Call<SingleResponse<LocalizedStrings>> getNewsRelModels();
}
