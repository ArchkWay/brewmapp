package ru.frosteye.beermap.app.di.module;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.twitter.sdk.android.core.Twitter;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.qualifier.ApiUrl;
import ru.frosteye.beermap.app.environment.BeerMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.common.ApiClient;
import ru.frosteye.beermap.execution.exchange.common.RestClient;
import ru.frosteye.beermap.execution.social.SocialManager;
import ru.frosteye.ovsa.di.module.BaseAppModule;
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.*;


@Module
public class AppModule extends BaseAppModule<BeerMap> {

    public AppModule(BeerMap context) {
        super(context);
        RxPaparazzo.register(context);
        VKSdk.initialize(context);
        Twitter.initialize(context);
        Fabric.with(context, new Crashlytics());
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
    Api provideApi(ApiClient apiClient) {
        return apiClient.getApi();
    }

    @Provides @ApiUrl
    String provideApiUrl() {
        return context.getString(R.string.config_api_url);
    }

    @Provides @Singleton
    IdentityProvider provideIdentityProvider(UserRepo repo) {
        return repo;
    }

    @Provides @Singleton
    SocialManager provideSocialManager() {
        return new SocialManager(context);
    }
}
