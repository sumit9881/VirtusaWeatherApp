package com.virtusa.virtusaweatherapp.model.dagger;

import com.virtusa.virtusaweatherapp.view.WeatherFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sadhika on 6/11/17.
 */

@Singleton
@Component(modules = {ApplicationModule.class, RetrofitModule.class})
public interface NetComponent {
    void inject(WeatherFragment fragment);
}
