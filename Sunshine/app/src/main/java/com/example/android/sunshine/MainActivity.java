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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mWeatherDisplayTextView = (TextView) findViewById(R.id.tv_weather_data);

        // perform the network request to get the weather
        this.loadWeatherData();
    }

    /**
     * A method will get the user's preferred location and execute the new AsyncTask.
     */
    private void loadWeatherData() {
        URL url = NetworkUtils.buildUrl(SunshinePreferences.getPreferredWeatherLocation(this));
        new FetchWeatherTask().execute(url);
    }

    /**
     * Class performs the network request
     */
    public class FetchWeatherTask extends AsyncTask<URL, Void, String[]> {

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
        protected void onPostExecute(String[] s) {
            if(s != null) {
                // iterate over weather data and display the results of the network request
                for(String data : s)
                    mWeatherDisplayTextView.append(data + "\n\n\n");
            }
        }
    }
}