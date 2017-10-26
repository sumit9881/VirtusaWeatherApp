package com.virtusa.virtusaweatherapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.virtusa.virtusaweatherapp.model.pojos.Main;
import com.virtusa.virtusaweatherapp.model.pojos.WeatherResult;
import com.virtusa.virtusaweatherapp.model.retrofit.WeatherService;
import com.virtusa.virtusaweatherapp.presenter.WeatherFragmentPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.OngoingStubbing;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sadhika on 10/25/17.
 */

public class WeatherFragmentPresenterUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    WeatherService mService;

    @Mock
    Context mContext;

    @Before
    public void setMockitoRule() {


        Mockito.when(mService.getWeather("abc, def, gh", Constants.APP_Id)).thenReturn(Single.fromCallable(new Callable<WeatherResult>() {
            @Override
            public WeatherResult call() throws Exception {
                WeatherResult result = new WeatherResult();

                result.setName("abc");
                Main main = new Main();
                main.setHumidity(20);
                main.setTemp(300d);
                main.setTempMax(330d);
                main.setTempMin(250d);
                result.setMain(main);
                return result;
            }
        }));
    }

    @BeforeClass
    public static void setUpRxSchedulers() {
        final Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable command) {

                    }
                });
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });

        RxJavaPlugins.setInitComputationSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });

        RxJavaPlugins.setInitNewThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitSingleSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return immediate;
            }
        });
    }
    @Test
    public void dataShouldBeLoaded()  {
        WeatherFragmentPresenter presenter = new WeatherFragmentPresenter(mContext, new WeatherFragmentPresenter.UIUpdater() {
            @Override
            public void updateName(String city) {

            }

            @Override
            public void updateWeatherIcon(String weatherImageUrl) {

            }

            @Override
            public void updateWeatherIconDefault() {

            }

            @Override
            public void updateWeatherCondition(String weather) {

            }

            @Override
            public void updateTemperature(String currentTemprature, String maxTemp, String minTemp) {

            }

            @Override
            public void upateHumidity(String humidity) {
              Assert.assertEquals(humidity, "21");

            }

            @Override
            public void showError(int errorCode) {

            }

            @Override
            public void showError(String message) {

            }
        }, mService);

        presenter.getWeatherInfo("abc, def, gh");

    }

}
