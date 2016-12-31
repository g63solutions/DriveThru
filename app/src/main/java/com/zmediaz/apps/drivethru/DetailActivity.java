package com.zmediaz.apps.drivethru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Computer on 12/30/2016.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE_SHARE_HASHTAG = " #DRIVETHRU";

    private String mMovieSummary;
    private TextView mMovieDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* Typical usage of findViewById... */
        mMovieDisplay = (TextView) findViewById(R.id.tv_detail);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovieSummary = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mMovieDisplay.setText(mMovieSummary);
            }
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mMovieSummary + MOVIE_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }
}
