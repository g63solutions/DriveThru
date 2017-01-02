package com.zmediaz.apps.drivethru;


import android.content.Intent;

import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.zmediaz.apps.drivethru.data.AppPreferences;
import com.zmediaz.apps.drivethru.utilities.NetworkUtils;
import com.zmediaz.apps.drivethru.utilities.TMDBJsonUtils;


import java.net.URL;

// TODO 2 Implement OnSharedPreferenceChangeListener on MainActivity.
// If you register a listener you must unregister it in on destroy!!!
public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String[]>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    String mKey;


    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    //ATL 1 You need an ID to identify the loader its the first parameter
    private static final int MOVIE_LOADER_INT = 7;

    // TODO 3 Add a private static boolean flag for preference updates and
    // initialize it to false
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_movie);
//
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_display);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mKey = getString(R.string.api);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //ATL 2 Prepare the loader in onCreate.  Either re-connect with an existing one,
        // or start a new one
        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(MOVIE_LOADER_INT, bundleForLoader, callback);

        // TODO 5 Register MainActivity as a OnSharedPreferenceChangedListener in onCreate
        /*
         * Register MainActivity as an OnPreferenceChangedListener to receive a callback when a
         * SharedPreference has changed. Please note that we must unregister MainActivity as an
         * OnSharedPreferenceChanged listener in onDestroy to avoid any memory leaks.
         */
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        /*TODO 3.5 Flag tied to this on change loder is restarted
         * If the preferences for location or units have changed since the user was last in
         * MainActivity, perform another query and set the flag to false.
         *
         * This isn't the ideal solution because there really isn't a need to perform another
         * GET request just to change the units, but this is the simplest solution that gets the
         * job done for now.
         */
        // TODO 6 In onStart, if preferences have been changed, refresh the
        // data and set the flag to false
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");

            getSupportLoaderManager().restartLoader(MOVIE_LOADER_INT, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    // TODO 7 Override onDestroy and unregister MainActivity as a SharedPreferenceChangedListener
    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();


        if (itemThatWasClickedId == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, Settings.class);
            startActivity(startSettingsActivity);
            return true;
        }
        // If you do NOT handle the menu click,
        // return super.onOptionsItemSelected to let Android handle the menu click
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String movie) {


        Class detail = DetailActivity.class;
        Intent startDetailClassIntent = new Intent(this, detail);
        startDetailClassIntent.putExtra(Intent.EXTRA_TEXT, movie);
        startActivity(startDetailClassIntent);
    }


    //ATL 5 Returns a String[] to onLoadFinished
    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {

            /*ATL 6 This String array will hold and help cache our weather data from deliverResult  */
            String[] mMovieJson = null;

            @Override
            protected void onStartLoading() {

               /* if (args == null) {
                    return;
                }*/
                //ATL Load data if it has been destroyed and recreated
                if (mMovieJson != null) {
                    deliverResult(mMovieJson);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {

                // TODO 8 Use sharedPreferences
                String queryUrl = AppPreferences.isPopular(MainActivity.this);

                URL mMovieUrl = NetworkUtils.buildUrl(queryUrl, mKey);

                if (queryUrl == null || TextUtils.isEmpty(queryUrl)) {
                    return null;
                }

                try {

                    String jsonData = NetworkUtils
                            .getResponseFromHttpUrl(mMovieUrl);

                    String[] simpleJsonMovieData = TMDBJsonUtils
                            .getSimpleMovieStringsFromJson(MainActivity.this, jsonData);

                    return
                            simpleJsonMovieData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String[] data) {

                mMovieJson = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMovieData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showJsonDataView();

        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    private void showErrorMessage() {
        // First, hide the currently visible data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    // TODO 4 Override onSharedPreferenceChanged to set the preferences flag to true
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {


        PREFERENCES_HAVE_BEEN_UPDATED = true;

    }


}


/*
Toast.makeText(context, "This Is A Toast Android", Toast.LENGTH_SHORT)
        .show();*/
