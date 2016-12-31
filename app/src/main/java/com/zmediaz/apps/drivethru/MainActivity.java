package com.zmediaz.apps.drivethru;

import android.content.Context;
import android.content.Intent;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zmediaz.apps.drivethru.utilities.NetworkUtils;
import com.zmediaz.apps.drivethru.utilities.TMDBJsonUtils;


import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String[]> {

    private static final String MOVIE_URL = "popular";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    String mKey;
    private String mSelector;
    private MenuItem mMenuItem;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    //TODO 1 You need an ID to identify the loader its the first parameter
    private static final int LOADER_INT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
//
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_display);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mKey = getString(R.string.api);
        mSelector = "popular";

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        loadMain();

        //TODO 2 Prepare the loader in onCreate.  Either re-connect with an existing one, or start a new one
        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;
        getSupportLoaderManager().initLoader(LOADER_INT, null, callback);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        mMenuItem = menu.findItem(R.id.action_search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {

            loadMainMenu();

            Context context = MainActivity.this;
            String textToShow = "Sort Menu clicked";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
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

    //Loads menu when app starts
    private void loadMain() {

        if (mSelector.equals("popular")) {

            urlQuery(mSelector);
            mSelector = "top_rated";

        } else {
            urlQuery(mSelector);
            mSelector = "popular";
        }
    }

    //Loads Main menu from button press
    private void loadMainMenu() {

        String mPopular = "Popular";
        String mTopRated = "Top Rated";

        if (mSelector.equals("popular")) {

            urlQuery(mSelector);
            mSelector = "top_rated";
            mMenuItem.setTitle(mPopular);
        } else {
            urlQuery(mSelector);
            mSelector = "popular";
            mMenuItem.setTitle(mTopRated);
        }
    }

    /*TODO 3 If your loader requires a variable bundle it. Its an args get the args back with the keys.*/
    private void urlQuery(String selection) {
        URL movieRequestUrl = NetworkUtils.buildUrl(selection, mKey);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(MOVIE_URL, movieRequestUrl.toString());

        /*TODO 4 LoadManager Starts it pass the ID and the bundle*/
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieSearchLoader = loaderManager.getLoader(LOADER_INT);
        if (movieSearchLoader == null) {
            loaderManager.initLoader(LOADER_INT, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER_INT, queryBundle, this);
        }

    }


    //TODO 5 Returns a String[] to onLoadFinished
    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {

            /*TODO 6 This String array will hold and help cache our weather data from deliverResult  */
            String[] mMovieJson = null;

            @Override
            protected void onStartLoading() {

                if (args == null) {
                    return;
                }
                //TODO Load data if it has been destroyed and recreated
                if (mMovieJson != null) {
                    deliverResult(mMovieJson);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {
                String queryUrl = args.getString(MOVIE_URL);

                
                if (queryUrl == null || TextUtils.isEmpty(queryUrl)) {
                    return null;
                }

                try {
                    URL mMovieUrl = new URL(queryUrl);

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
}


/*
Toast.makeText(context, "This Is A Toast Android", Toast.LENGTH_SHORT)
        .show();*/
