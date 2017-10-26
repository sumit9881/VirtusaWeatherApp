package com.virtusa.virtusaweatherapp.model.dagger;

import android.app.Application;
import android.content.Context;

import com.virtusa.virtusaweatherapp.VirtusaWeatherApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sadhika on 6/11/17.
 */

@Module
public class ApplicationModule {

    Context mContext;
    Application mApplication;

    public ApplicationModule(Context ctx, Application application) {
        mContext = ctx;
        mApplication = application;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return mApplication;
    }


    @Provides
    @Singleton
    public Context providesContext() {
        return mContext;
    }
}
