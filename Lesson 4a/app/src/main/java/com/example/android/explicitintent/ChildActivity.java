package com.example.android.explicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ChildActivity extends AppCompatActivity {

    private TextView mDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        mDisplayTextView = (TextView) findViewById(R.id.tv_display);

        Intent intentThatSharedThisActivity = getIntent();
        if(intentThatSharedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String textEntered = intentThatSharedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            mDisplayTextView.setText(textEntered);
        }
    }
}
