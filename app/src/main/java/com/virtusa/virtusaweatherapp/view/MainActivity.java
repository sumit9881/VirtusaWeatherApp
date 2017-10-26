package com.virtusa.virtusaweatherapp.view;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.virtusa.virtusaweatherapp.R;

public class MainActivity extends AppCompatActivity implements WeatherFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, WeatherFragment.newInstance(), null).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
