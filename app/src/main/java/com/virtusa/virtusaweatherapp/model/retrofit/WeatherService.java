package com.virtusa.virtusaweatherapp.model.retrofit;

import com.virtusa.virtusaweatherapp.model.pojos.WeatherResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sadhika on 10/24/17.
 */

public interface WeatherService {
    @GET("weather")
    Single<WeatherResult> getWeather(@Query("q") String location, @Query("APPID") String appId);

}
