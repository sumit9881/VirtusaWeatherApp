package com.virtusa.virtusaweatherapp;

import android.app.Application;

import com.virtusa.virtusaweatherapp.model.dagger.ApplicationModule;
import com.virtusa.virtusaweatherapp.model.dagger.DaggerNetComponent;
import com.virtusa.virtusaweatherapp.model.dagger.NetComponent;
import com.virtusa.virtusaweatherapp.model.dagger.RetrofitModule;

/**
 * Created by Sadhika on 10/24/17.
 */

public class VirtusaWeatherApp extends Application {
    private NetComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerNetComponent.builder().applicationModule(new ApplicationModule(getApplicationContext(), this)).retrofitModule(new RetrofitModule()).build();

    }

    public NetComponent getComponent() {
        return mComponent;
    }


}
