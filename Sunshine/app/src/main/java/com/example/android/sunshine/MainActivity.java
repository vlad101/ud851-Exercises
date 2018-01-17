/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.ForecastAdapterOnClickHnadler {

    //private TextView mWeatherTextView;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);

        // load data
        loadWeatherData();
    }

    /**
     * A method will get the user's preferred location and execute the new AsyncTask.
     */
    private void loadWeatherData() {
        URL url = NetworkUtils.buildUrl(SunshinePreferences.getPreferredWeatherLocation(this));
        new FetchWeatherTask().execute(url);
    }

//    ????? Something with a  a toast TODO

    /**
     * Class performs the network request
     */
    public class FetchWeatherTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show the loading indicator
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            String[] respArr = null;
            String respWeatherData = null;
            try {
                if(urls.length > 0) {
                    // perform the network request
                    respWeatherData = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                    // get simple weather strings form json
                    respArr = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, respWeatherData);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return respArr;
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(weatherData != null && weatherData.length > 0) {
                // hide the error, show the data
                showWeatherDataView();
                // iterate over weather data and display the results of the network request
                mForecastAdapter.setmWeatherData(weatherData);
            } else {
                // hide the data, show the error
                this.showErrorMessage();
            }
        }

        /**
         * Method will hide the error message and show the weather data.
         */
        private void showErrorMessage() {
            // hide the weather data
            mRecyclerView.setVisibility(View.INVISIBLE);
            // show the error message
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }

        /**
         * Method will hide the error message and show the weather data.
         */
        private void showWeatherDataView() {
            // hide the error message
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            // show the weather data
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /*
        Menu methods.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                // clear forecast adapter
                mForecastAdapter.setmWeatherData(null);
                // re-load data
                loadWeatherData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(String dayWeather) {
        Toast.makeText(this, dayWeather, Toast.LENGTH_SHORT).show();
    }
}