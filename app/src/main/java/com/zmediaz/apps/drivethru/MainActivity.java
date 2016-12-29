package com.zmediaz.apps.drivethru;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zmediaz.apps.drivethru.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mMainTV;
    private String mKey;
    private String mSelector;
    private MenuItem mMenuItem;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
//
        mMainTV = (TextView) findViewById(R.id.tv_display);
        mKey = getString(R.string.api);
        mSelector = "popular";

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMain();
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


    private void urlQuery(String selection) {
        URL selectionUrl = NetworkUtils.buildUrl(selection, mKey);

        new getHttpTask().execute(selectionUrl);
    }

    /*Network Thread call "new getHttpTask().execute(selectionUrl);"
    * pass in the parameter for the background task. doInBackground(URL... params)
    * is a params array that's why it has 0 the first item in array. You return to the post execute*/
    public class getHttpTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String jsonData = null;
            try {
                jsonData = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        //After thread has finished executing return from do in background goes here
        @Override
        protected void onPostExecute(String jsonData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (jsonData != null && !jsonData.equals("")) {
                showJsonDataView();
                mMainTV.setText(jsonData);
            } else {
                showErrorMessage();
            }
        }

        private void showJsonDataView() {
            // First, make sure the error is invisible
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            // Then, make sure the JSON data is visible
            mMainTV.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage() {
            // First, hide the currently visible data
            mMainTV.setVisibility(View.INVISIBLE);
            // Then, show the error
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }
    }
}


//Create error message that shows if data is not null or empty
//Create Progress Bar that is initially invisible and shows as soon as thread is finished.
