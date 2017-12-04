package com.brewmapp.execution.exchange.common;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import com.brewmapp.data.entity.Album;
import com.brewmapp.data.entity.AverageEvaluation;
import com.brewmapp.data.entity.BeerBrand;
import com.brewmapp.data.entity.BeerLocation;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.Evaluation;
import com.brewmapp.data.entity.FilterBeerLocation;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.FeatureTypes;
import com.brewmapp.data.entity.KitchenTypes;
import com.brewmapp.data.entity.Models;
import com.brewmapp.data.entity.PriceRangeTypes;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.entity.RestoTypes;
import com.brewmapp.data.entity.Sales;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.container.AlbumPhotos;
import com.brewmapp.data.entity.container.Albums;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.container.Events;
import com.brewmapp.data.entity.container.FilterBeer;
import com.brewmapp.data.entity.container.Interests;
import com.brewmapp.data.entity.container.InterestsByUser;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.container.Beers;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.data.entity.container.Restos;
import com.brewmapp.data.entity.container.Reviews;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.entity.container.Users;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.data.pojo.BeerTypes;
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

    @GET("quick_search/{query}")
    Call<MessageResponse> quickSearch(@Path("query") String query,
                                      @Query("lat") double lat, @Query("lon") double lon);

    @POST("search/resto")
    @FormUrlEncoded
    Call<ListResponse<Resto>> findRestos(@QueryMap RequestParams query,
                                         @FieldMap RequestParams params);

    @POST("subscription")
    @FormUrlEncoded
    Call<ListResponse<Subscription>> loadUserSubscriptionsList(@FieldMap WrapperParams params);


    @POST("geo/location")
    @FormUrlEncoded
    Call<ListResponse<BeerLocation.LocationInfo>> loadLocationById(@FieldMap WrapperParams params);

    @GET("resto/allrestoincity")
    Call<ListResponse<RestoLocation>> loadRestoLocationInCity(@Query(Keys.CITY_ID) int cityId);

    @POST("geo/city")
    @FormUrlEncoded
    Call<ListResponse<City>> loadCity(@FieldMap WrapperParams params);

//    @GET("geocode/json")
//    Call<GeoСodeResponse> getLocation(
//            @Query("address") String address,w
//            @Query("key") String key
//    );

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
    Call<Restos> fullSearchResto(
            @Path("query") String query,
            @Query(Keys.LIMIT_START) int start,
            @Query(Keys.LIMIT_END) int end,
            @FieldMap WrapperParams params
    );

    @POST("resto/restodata")
    @FormUrlEncoded
    Call<RestoDetails> getRestoDetails(@Query(Keys.RESTO_ID) String query, @FieldMap WrapperParams params);

    @GET("resto/type")
    Call<RestoTypes> loadRestoTypes();

    @GET("resto/kitchen")
    Call<KitchenTypes> loadKitchenTypes();

    @GET("resto/pricerange")
    Call<PriceRangeTypes> loadPriceRanges();

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
//    @POST("reviews")
//    @FormUrlEncoded
//    Call<ListResponse<Review>> loadReviews(@FieldMap WrapperParams params);

    @POST("/api/resto/restoevaluation")
    @FormUrlEncoded
    Call<ListResponse<Evaluation>> getRestoEvaluation(@FieldMap WrapperParams params);

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
    Call<ListResponse<FilterRestoLocation>> loadRestoLocation(@FieldMap RequestParams requestParams);

    @POST("beer/getcoordinates")
    @FormUrlEncoded
    Call<ListResponse<FilterBeerLocation>> loadBeerLocation(@FieldMap RequestParams requestParams);

    @POST("profile")
    @FormUrlEncoded
    Call<ListResponse<User>> profileEdit(@FieldMap WrapperParams params);

    @POST("api/resto/getcoordinates")
    @FormUrlEncoded
    Call<Object> loadRestoGeo(@FieldMap WrapperValues params);

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
    Call<BeerTypes> loadBeerTypes();

    @POST("beer/type")
    @FormUrlEncoded
    Call<com.brewmapp.data.pojo.BeerBrand> loadBeerBrands();

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

}
