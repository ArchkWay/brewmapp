package com.brewmapp.execution.exchange.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.frosteye.ovsa.execution.network.client.IdentityProvider;

public abstract class BaseRetrofitClient<T> {
    private T api;
    private IdentityProvider identityProvider;
    /** @deprecated */
    @Deprecated
    private String token;

    public BaseRetrofitClient(String baseUrl) {
        this.init(baseUrl, (IdentityProvider)null);
    }

    public BaseRetrofitClient(String baseUrl, IdentityProvider identityProvider) {
        this.init(baseUrl, identityProvider);
    }

    private void init(String baseUrl, IdentityProvider identityProvider) {
        this.identityProvider = identityProvider;
        Builder clientBuilder = new Builder();
        List<Interceptor> interceptors = this.createClientInterceptors();
        if (interceptors != null) {
            Iterator var5 = interceptors.iterator();

            while(var5.hasNext()) {
                Interceptor interceptor = (Interceptor)var5.next();
                clientBuilder.addInterceptor(interceptor);
            }
        }

        clientBuilder.connectTimeout(30L, TimeUnit.SECONDS);
        clientBuilder.readTimeout(60L, TimeUnit.SECONDS);
        Gson gson = this.createGsonBuilder().create();
        if (gson == null) {
            gson = new Gson();
        }

        Retrofit retrofit = (new retrofit2.Retrofit.Builder()).baseUrl(baseUrl).client(clientBuilder.build()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        this.api = retrofit.create(this.apiClass());
    }

    /** @deprecated */
    @Deprecated
    public void setToken(String token) {
        this.token = token;
    }

    public T getApi() {
        return this.api;
    }

    public abstract Class<T> apiClass();

    protected GsonBuilder createGsonBuilder() {
        return (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    protected List<Interceptor> createClientInterceptors() {
        List<Interceptor> interceptors = new ArrayList();
        interceptors.add(new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request.Builder builder = chain.request().newBuilder();
                if (BaseRetrofitClient.this.getToken() != null) {
                    builder.addHeader(BaseRetrofitClient.this.getAuthHeaderName(), BaseRetrofitClient.this.getAuthHeaderValuePrefix() + BaseRetrofitClient.this.getToken());
                }

                Iterator var3 = BaseRetrofitClient.this.getHeaders().entrySet().iterator();

                while(var3.hasNext()) {
                    Entry<String, String> entry = (Entry)var3.next();
                    builder.addHeader((String)entry.getKey(), (String)entry.getValue());
                }

                return chain.proceed(builder.build());
            }
        });
        interceptors.add((new HttpLoggingInterceptor()).setLevel(Level.BODY));
        return interceptors;
    }

    protected String getAuthHeaderName() {
        return "Authorization";
    }

    protected String getAuthHeaderValuePrefix() {
        return "";
    }

    protected Map<String, String> getHeaders() {
        return new HashMap();
    }

    protected String getToken() {
        return this.identityProvider != null ? this.identityProvider.provideIdentity() : this.token;
    }
}
