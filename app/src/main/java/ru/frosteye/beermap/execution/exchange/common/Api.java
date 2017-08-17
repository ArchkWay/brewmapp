package ru.frosteye.beermap.execution.exchange.common;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import ru.frosteye.beermap.data.entity.Album;
import ru.frosteye.beermap.data.entity.Contact;
import ru.frosteye.beermap.data.entity.container.AlbumPhotos;
import ru.frosteye.beermap.data.entity.container.Albums;
import ru.frosteye.beermap.data.entity.Post;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.beermap.data.entity.container.Posts;
import ru.frosteye.beermap.data.entity.wrapper.ContactInfo;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.response.UploadPhotoResponse;
import ru.frosteye.beermap.execution.exchange.response.base.SingleResponse;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.beermap.execution.exchange.response.base.ListResponse;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.ovsa.execution.network.request.MultipartRequestParams;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 25.07.17.
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
    Call<Posts> loadPosts(@FieldMap WrapperParams params);

    @POST("photoalbum/add")
    @FormUrlEncoded
    Call<SingleResponse<Album>> createAlbum(@FieldMap WrapperParams params);

    @POST("news/add")
    @FormUrlEncoded
    Call<SingleResponse<Post>> createPost(@FieldMap WrapperParams params);

    @POST("photo/add")
    @Multipart
    Call<SingleResponse<UploadPhotoResponse>> uploadPhoto(@PartMap MultipartRequestParams params);

    @POST("photo")
    @FormUrlEncoded
    Call<AlbumPhotos> loadPhotosForAlbum(@FieldMap WrapperParams params);


}
