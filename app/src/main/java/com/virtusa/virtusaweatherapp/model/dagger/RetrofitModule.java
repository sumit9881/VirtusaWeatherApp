package com.virtusa.virtusaweatherapp.model.dagger;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.virtusa.virtusaweatherapp.model.retrofit.WeatherService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sadhika on 6/11/17.
 */

@Module
public class RetrofitModule {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory convertorFactory, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .addConverterFactory(convertorFactory)
                .build();
    }

    @Provides
    @Singleton
    WeatherService provideWeatherService(Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }

    @Provides
    @Singleton
    GsonConverterFactory provideConverterFactory(Gson gson) {
        return  GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    Gson proviesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder().cache(cache).build();
    }

    @Provides
    @Singleton
    Cache providesCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }
}
