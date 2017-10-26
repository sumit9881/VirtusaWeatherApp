package com.virtusa.virtusaweatherapp.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.virtusa.virtusaweatherapp.Constants;
import com.virtusa.virtusaweatherapp.model.pojos.Main;
import com.virtusa.virtusaweatherapp.model.pojos.Weather;
import com.virtusa.virtusaweatherapp.model.pojos.WeatherResult;
import com.virtusa.virtusaweatherapp.model.retrofit.WeatherService;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sadhika on 10/24/17.
 */

public class WeatherFragmentPresenter {

    private static final String EXT_PNG = ".png";

    UIUpdater mListener;
    WeatherService mService;
    Context mContext;

    public WeatherFragmentPresenter(Context ctx, UIUpdater listener, WeatherService service) {
        mContext = ctx;
        mListener = listener;
        mService = service;
    }

    /**
     * Get weather information of passed location
     *
     * @param location
     */

    public void getWeatherInfo(String location) {
        if (location.isEmpty()) {
            mListener.showError(Constants.ERROR_MISSING_PARAM);
        }
        mService.getWeather(location, Constants.APP_Id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<WeatherResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull WeatherResult weatherResult) {
                        List<Weather> weathers = weatherResult.getWeather();
                        Main mainWeatherInfo = weatherResult.getMain();
                        if (weathers == null || weathers.size() <= 0 || mainWeatherInfo == null) {
                            mListener.showError(Constants.ERROR_MISSING_PARAM);
                            return;
                        }
                        saveDataToSharedPreference(weatherResult);
                        mListener.updateName(weatherResult.getName());
                        updateTemperature(mainWeatherInfo.getTemp(), mainWeatherInfo.getTempMax(), mainWeatherInfo.getTempMin());
                        mListener.upateHumidity(mainWeatherInfo.getHumidity().toString());
                        updateWeather(weathers.get(0));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mListener.showError(e.getMessage());
                    }
                });
    }

    private void updateTemperature(double cur, double max, double min) {
        mListener.updateTemperature(kelvinToFH(cur), kelvinToFH(max), kelvinToFH(min));
    }

    private void updateWeather(Weather weather) {
        String imageUrl = Constants.IMAGE_BASE_URL + weather.getIcon() + EXT_PNG;
        mListener.updateWeatherCondition(weather.getDescription());
        mListener.updateWeatherIcon(imageUrl);
    }

    private String kelvinToFH(double kelvin) {
        double result;
        result = 9 / 5 * (kelvin - 273) + 32;
        return String.format("%.2f", result);
    }

    /**
     * restore data when app starts
     *
     * @param
     */
    public void restoreData() {
        // TODO: have to think over moving it to secondary thread.
        SharedPreferences sharedPref = mContext.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //city name
        mListener.updateName(sharedPref.getString(Constants.CITY_NAME, Constants.DEFAULT_VALUE));

        // current weather condition
        mListener.updateWeatherCondition(sharedPref.getString(Constants.WEATHER_CONDITION, Constants.DEFAULT_VALUE));

        // weather imager
        String imagePath = sharedPref.getString(Constants.IMAGE_PATH, "");
        if (!imagePath.isEmpty()) {
            mListener.updateWeatherIcon(imagePath);
        } else {
            mListener.updateWeatherIconDefault();
        }

        // temperature update
        mListener.updateTemperature(sharedPref.getString(Constants.CURRENT_TEMP, Constants.DEFAULT_VALUE),
                sharedPref.getString(Constants.MAX_TEMP, Constants.DEFAULT_VALUE),
                sharedPref.getString(Constants.MIN_TEMP, Constants.DEFAULT_VALUE));

        mListener.upateHumidity(sharedPref.getString(Constants.HUMIDITY, Constants.DEFAULT_VALUE));


    }


    private Single<Boolean> saveWeatherResultObservable(final WeatherResult weatherResult) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                SharedPreferences sharedPref = mContext.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                //city name
                editor.putString(Constants.CITY_NAME, weatherResult.getName());

                Weather weather = weatherResult.getWeather().get(0);
                // current weather condition
                editor.putString(Constants.WEATHER_CONDITION, weather.getDescription());

                // current weather's image path
                editor.putString(Constants.IMAGE_PATH, Constants.IMAGE_BASE_URL + weather.getIcon() + EXT_PNG);

                Main main = weatherResult.getMain();
                // current temp
                editor.putString(Constants.CURRENT_TEMP, kelvinToFH(main.getTemp()));
                editor.putString(Constants.MAX_TEMP, kelvinToFH(main.getTempMax()));
                editor.putString(Constants.MIN_TEMP, kelvinToFH(main.getTempMin()));

                // humidity
                editor.putString(Constants.HUMIDITY, main.getHumidity().toString());
                return editor.commit();
            }

        });
    }

    private void saveDataToSharedPreference(final WeatherResult weatherResult) {
        saveWeatherResultObservable(weatherResult).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        // TODO: have to figure out how to handle this scenario

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // TODO: have to figure out how to handle this scenario
                    }
                });

    }


    public interface UIUpdater {
        void updateName(String city);

        void updateWeatherIcon(String weatherImageUrl);

        void updateWeatherIconDefault();

        void updateWeatherCondition(String weather);

        void updateTemperature(String currentTemprature, String maxTemp, String minTemp);

        void upateHumidity(String humidity);

        void showError(int errorCode);

        void showError(String message);
    }
}
