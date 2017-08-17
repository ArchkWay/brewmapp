package ru.frosteye.beermap.execution.social;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;

/**
 * Created by oleg on 17.08.17.
 */

public class CustomTwitterClient extends TwitterApiClient {
    public CustomTwitterClient(TwitterSession session) {
        super(session);
    }

    public FollowersService getFollowers() {
        return getService(FollowersService.class);
    }

    public interface FollowersService {
        @GET("/1.1/followers/list.json")
        Call<TwitterUsers> listTwitterFriends(@Query("user_id") String userId,
                                              @Query("screen_name") String var,
                                              @Query("skip_status") Boolean var1,
                                              @Query("include_user_entities") Boolean var2,
                                              @Query("count") Integer var3);

        @POST("/1.1/direct_messages/new.json")
        @FormUrlEncoded
        Call<Dummy> sendMessage(@Field("text") String text,
                                              @Query("user_id") String userId);
    }

    public static class TwitterUsers {
        private List<TwitterUser> users;

        public List<TwitterUser> getUsers() {
            return users;
        }
    }

    public static class Dummy {
    }

    public static class TwitterUser {
        private String id;
        private String name;

        @SerializedName(Keys.PROFILE_IMAGE_URL)
        private String profileImage;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getProfileImage() {
            return profileImage;
        }
    }
}
