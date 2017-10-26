package com.virtusa.virtusaweatherapp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.virtusa.virtusaweatherapp.Constants;
import com.virtusa.virtusaweatherapp.R;
import com.google.android.gms.plus.PlusOneButton;
import com.virtusa.virtusaweatherapp.VirtusaWeatherApp;
import com.virtusa.virtusaweatherapp.model.retrofit.WeatherService;
import com.virtusa.virtusaweatherapp.presenter.WeatherFragmentPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherFragment extends Fragment implements WeatherFragmentPresenter.UIUpdater {

    @Inject
    WeatherService mWeatherService;
    @Inject
    Context mContext;


    @BindView(R.id.edittext_searchtext)
    EditText mEditTextSearch;

    @BindView(R.id.textview_city)
    TextView mTextViewCity;

    @BindView(R.id.imageview_weather)
    ImageView mImageView;

    @BindView(R.id.textview_weather)
    TextView mTextViewWeather;

    @BindView(R.id.textview_curtemp)
    TextView mTextViewCurTemp;

    @BindView(R.id.textview_maxtemp)
    TextView mTextViewMaxTemp;

    @BindView(R.id.textview_mintemp)
    TextView mTextViewMinTemp;

    @BindView(R.id.textview_humidity)
    TextView mTextViewHumid;

    private View mView;

    private OnFragmentInteractionListener mListener;
    private WeatherFragmentPresenter mPresenter;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((VirtusaWeatherApp) getActivity().getApplication()).getComponent().inject(this);
        mPresenter = new WeatherFragmentPresenter(mContext, this, mWeatherService);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mView = inflater.inflate(R.layout.fragment_weather, container, false);
            ButterKnife.bind(this, mView);
        }
        mPresenter.restoreData();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.button_search)
    void search(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
        mPresenter.getWeatherInfo(mEditTextSearch.getText().toString());
    }

    @Override
    public void updateName(String city) {
        mTextViewCity.setText(city);
    }

    @Override
    public void updateWeatherIcon(String weatherImageUrl) {
        Glide.with(mContext)
                .load(weatherImageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(mImageView);
    }

    @Override
    public void updateWeatherIconDefault() {
        Glide.with(mContext)
                .load(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(mImageView);

    }

    @Override
    public void updateWeatherCondition(String weather) {
        mTextViewWeather.setText(weather);
    }


    @Override
    public void updateTemperature(String currentTemprature, String maxTemp, String minTemp) {
        mTextViewCurTemp.setText(currentTemprature);
        mTextViewMaxTemp.setText(getString(R.string.max_temp, maxTemp));
        mTextViewMinTemp.setText(getString(R.string.min_temp, minTemp));
    }

    @Override
    public void upateHumidity(String humidity) {
        mTextViewHumid.setText(getString(R.string.humidity, humidity));
    }

    @Override
    public void showError(int errorCode) {
        String message = null;
        switch (errorCode) {
            case Constants.ERROR_MISSING_PARAM:
                message = getString(R.string.missing_params);
                break;
            case Constants.ERROR_BAD_RESULT:
                message = getString(R.string.wrong_response);
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), null);
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
