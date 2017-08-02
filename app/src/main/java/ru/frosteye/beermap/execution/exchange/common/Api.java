package ru.frosteye.beermap.execution.exchange.common;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;

/**
 * Created by oleg on 25.07.17.
 */

public interface Api {

    @POST("start/login")
    @FormUrlEncoded
    Call<UserResponse> login(@FieldMap WrapperParams params);

    @POST("start/register")
    @FormUrlEncoded
    Call<MessageResponse> register(@FieldMap WrapperParams params);
}
