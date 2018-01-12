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
package com.example.android.datafrominternet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView;
    TextView mSearchResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
    }

    /*
        Menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:
                this.makeGithubSearchQuery();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
    // TODO (3) Override onPostExecute to display the results in the TextView

    /**
     * Perfomr the query.
     */
    class GithubQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            // get response from getResponseFromHttpUrl
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            // display the results in mSearchResultsTextView
            if(s != null && s.length() > 0) {
                mSearchResultsTextView.setText(s);
            }
        }
    }

    /*
        Util Methods.
     */
    private void makeGithubSearchQuery() {
        // get the text from the EditText
        String query = mSearchBoxEditText.getText().toString();
        // build the URL with EditText query
        URL url = NetworkUtils.buildUrl(query);
        // set the built URL to the TextView
        mUrlDisplayTextView.setText(url.toString());
        // create a new GithubQueryTask and call its execute method, passing in the url to query
        new GithubQueryTask().execute(url);
    }


}
