package com.brewmapp.app.di.module;

import com.brewmapp.app.di.qualifier.ChatUrl;
import com.brewmapp.execution.exchange.common.ChatClient;
import com.brewmapp.execution.tool.HashTagHelper;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.twitter.sdk.android.core.Twitter;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;
import com.brewmapp.R;
import com.brewmapp.app.di.qualifier.ApiUrl;
import com.brewmapp.app.environment.BeerMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.common.ApiClient;
import com.brewmapp.execution.exchange.common.RestClient;
import com.brewmapp.execution.social.SocialManager;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import ru.frosteye.ovsa.di.module.BaseAppModule;
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.brewmapp.BuildConfig.SERVER_API_URL;


@Module
public class AppModule extends BaseAppModule<BeerMap> {

    public AppModule(BeerMap context) {
        super(context);
        RxPaparazzo.register(context);
        VKSdk.initialize(context);
        Twitter.initialize(context);
        Fresco.initialize(context);
        //Fabric.with(context, new Crashlytics());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    @Provides @Singleton
    ApiClient provideApiClient(RestClient client) {
        return client;
    }

    @Provides @Singleton
    HashTagHelper provideHashTtagHelper() {
        return new HashTagHelper(context);
    }

    @Provides @Singleton
    Api provideApi(ApiClient apiClient) {
        return apiClient.getApi();
    }

    @Provides @ApiUrl
    String provideApiUrl() {
        return SERVER_API_URL;
    }

    @Provides @Singleton
    IdentityProvider provideIdentityProvider(UserRepo repo) {
        return repo;
    }

    @Provides @Singleton
    SocialManager provideSocialManager() {
        return new SocialManager(context);
    }

    @Provides @ChatUrl
    String provideChatUrl() {
        return "https://chat.brewmapp.com:8443";
    }

    @Provides @Singleton
    Socket provideChatSocket(ChatClient chatClient) {return chatClient.getSocket();}

}
