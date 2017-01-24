package com.zmediaz.apps.drivethru;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zmediaz.apps.drivethru.data.MovieContract;

/**
 * Created by Computer on 12/30/2016.
 */

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MOVIE_SHARE_HASHTAG = " #DRIVETHRU";

    private String mMovieSummary;

    private Uri mUri;

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };

    public static final int INDEX_POSTER_PATH = 0;
    public static final int INDEX_OVERVIEW = 1;
    public static final int INDEX_RELEASE_DATE = 2;
    public static final int INDEX_MOVIE_ID = 3;
    public static final int INDEX_ORIGINAL_TITLE = 4;
    public static final int INDEX_BACKDROP_PATH = 5;
    public static final int INDEX_VOTE_AVERAGE = 6;

    private TextView mPosterPath;
    private TextView mOverview;
    private TextView mReleaseDate;
    private TextView mMovieId;
    private TextView mOriginalTitle;
    private TextView mBackdropPath;
    private TextView mVoteAverage;

    private static final int ID_DETAIL_LOADER = 2924;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayout_detail);

        mPosterPath = (TextView) findViewById(R.id.poster_path);
        mOverview = (TextView) findViewById(R.id.overview);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mMovieId = (TextView) findViewById(R.id.movie_id);
        mOriginalTitle = (TextView) findViewById(R.id.original_title);
        mBackdropPath = (TextView) findViewById(R.id.backdrop_path);
        mVoteAverage = (TextView) findViewById(R.id.vote_average);

        mUri = getIntent().getData();

        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, Settings.class);
            startActivity(startSettingsActivity);
            return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

        switch (loaderId) {
            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }


        String poster_path = data.getString(INDEX_POSTER_PATH);
        mPosterPath.setText(poster_path);
        String overview = data.getString(INDEX_OVERVIEW);
        mOverview.setText(overview);
        String release_date = data.getString(INDEX_RELEASE_DATE);
        mReleaseDate.setText(release_date);
        String movie_id = data.getString(INDEX_MOVIE_ID);
        mMovieId.setText(movie_id);
        String original_title = data.getString(INDEX_ORIGINAL_TITLE);
        mOriginalTitle.setText(original_title);
        String backdrop_path = data.getString(INDEX_BACKDROP_PATH);
        mBackdropPath.setText(backdrop_path);
        String vote_average = data.getString(INDEX_VOTE_AVERAGE);
        mVoteAverage.setText(vote_average);

        mMovieSummary = String.format("%s - %s - %s", original_title, release_date, vote_average);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

/*
35 35 35*/
