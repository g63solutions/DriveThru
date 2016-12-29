package com.zmediaz.apps.drivethru;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.zmediaz.apps.drivethru.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //    Field to Store the Movie display TextView
    private TextView mMovieTextView;
    private TextView mDisplay;
    private String mKey;
    private String mSelector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
//      View cast to TextView and stored in variable
        mMovieTextView = (TextView) findViewById(R.id.tv_movie_data);
        mDisplay = (TextView) findViewById(R.id.tv_display);
        mKey = getString(R.string.api);
        mSelector = "popular";


//      Dummy Data
        String[] dummyWeatherData = {
                "Today, May 17 - Clear - 17°C / 15°C",
                "Tomorrow - Cloudy - 19°C / 15°C",
                "Thursday - Rainy- 30°C / 11°C",
                "Friday - Thunderstorm - 21°C / 9°C",
                "Saturday - Thunderstorms - 16°C / 7°C",
                "Sunday - Rainy - 16°C / 8°C",
                "Monday - Partly Cloudy - 15°C / 10°C",
                "Tue, May 24 - Meatballs - 16°C / 18°C",
                "Wed, May 25 - Cloudy - 19°C / 15°C",
                "Thu, May 26 - Stormy - 30°C / 11°C",
                "Fri, May 27 - Hurricane - 21°C / 9°C",
                "Sat, May 28 - Meteors - 16°C / 7°C",
                "Sun, May 29 - Apocalypse - 16°C / 8°C",
                "Mon, May 30 - Post Apocalypse - 15°C / 10°C",
        };

        for (String dummyWeatherDay : dummyWeatherData) {
            mMovieTextView.append(dummyWeatherDay + "\n\n\n");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // COMPLETED (9) Within onCreateOptionsMenu, use
        // getMenuInflater().inflate to inflate the menu
        getMenuInflater().inflate(R.menu.main, menu);
        // COMPLETED (10) Return true to display your menu it
        // is a boolean return



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {


            if (mSelector == "popular") {

                makeGithubSearchQuery(mSelector);
                mSelector = "top_rated";
            } else {
                makeGithubSearchQuery(mSelector);
                mSelector = "popular";
            }

            Context context = MainActivity.this;
            String textToShow = "Search clicked";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            return true;
        }
        // If you do NOT handle the menu click,
        // return super.onOptionsItemSelected to let Android handle the menu click
        return super.onOptionsItemSelected(item);
    }

    private void makeGithubSearchQuery(String selection) {
        String githubQuery = selection;
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery, mKey);
        mDisplay.setText(githubSearchUrl.toString());
    }

}
