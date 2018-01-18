package com.example.android.sunshine;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private String dayWeatherStr = "";
    private TextView mDisplayForecastText;
    private static final String FORECAST_SHAPE_HASHTAG = "#SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDisplayForecastText = (TextView) findViewById(R.id.tv_display_forecast);
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String dayWeatherText = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            dayWeatherStr = dayWeatherText;
            mDisplayForecastText.setText(dayWeatherText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(shareText());
        return true;
    }

    private Intent shareText() {
        String mimeType = "text/plain";
        String title = "Sunshine - Weather Forecast App";
        Intent sharingIntent = ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(dayWeatherStr + " " + FORECAST_SHAPE_HASHTAG)
                .getIntent();
        return sharingIntent;
    }
}
