package com.brewmapp.execution.exchange.common;

import retrofit2.Call;
import retrofit2.http.Field;
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
import com.brewmapp.data.entity.BeerLocation;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.entity.Sales;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.container.AlbumPhotos;
import com.brewmapp.data.entity.container.Albums;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.container.Events;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
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

    @POST("event")
    @FormUrlEncoded
    Call<Events> loadEvents(@Query(Keys.LIMIT_START) int start,
                            @Query(Keys.LIMIT_END) int end,
                            @FieldMap WrapperParams params);

    @POST("shares")
    @FormUrlEncoded
    Call<Sales> loadSales(@Query(Keys.LIMIT_START) int start,
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
    Call<ListResponse<Subscription>> loadUserSubscriptions(@FieldMap WrapperParams params);

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

}
