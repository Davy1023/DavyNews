package com.davy.davy_news.module;

import com.davy.davy_news.DavyNewsApplication;
import com.davy.davy_news.net.ApiConstants;
import com.davy.davy_news.net.NewsApi;
import com.davy.davy_news.net.NewsApiService;
import com.davy.davy_news.net.RetrofitConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: Davy
 * date: 18/7/2
 */
@Module
public class HttpModule {

    @Provides
    OkHttpClient.Builder provideOkhttpClient(){

        Cache cache = new Cache(new File(DavyNewsApplication.getContext().getCacheDir(),"NewHttpCache"),1024 * 1024 * 100);
        return new OkHttpClient.Builder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(RetrofitConfig.sLoggingInterceptor)
                .addInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS);
    }

    @Provides
    NewsApi provideNewApi(OkHttpClient.Builder builder){

        builder.addInterceptor(RetrofitConfig.sQueryParameterInterceptor);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return NewsApi.getInstance(retrofitBuilder
                        .baseUrl(ApiConstants.fengApi)
                        .build().create(NewsApiService.class));
    }

}
