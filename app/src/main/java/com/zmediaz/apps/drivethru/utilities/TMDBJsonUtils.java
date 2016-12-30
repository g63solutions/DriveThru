package com.zmediaz.apps.drivethru.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Me
 */

public final class TMDBJsonUtils {

    public static String[] getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        /* Movie information. Each day's forecast info is an element of the "results" array */
        final String TMDB_RESULTS = "results";

        final String TMDB_POSTER_PATH = "poster_path";

        final String TMDB_OVERVIEW = "overview";

        final String TMDB_RELEASE_DATE = "release_date";

        final String TMDB_ID = "id";

        final String TMDB_ORIGINAL_TITLE = "original_title";

        final String TMDB_BACKDROP_PATH = "backdrop_path";

        final String TMDB_VOTE_AVERAGE = "vote_average";

        /* String array to hold Movie String */
        String[] parsedMovieData;


        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

        parsedMovieData = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            String poster_path;
            String overview;
            String release_date;
            String id;
            String original_title;
            String backdrop_path;
            String vote_average;


            JSONObject flick = movieArray.getJSONObject(i);

            poster_path = flick.getString(TMDB_POSTER_PATH);
            overview = flick.getString(TMDB_OVERVIEW);
            release_date = flick.getString(TMDB_RELEASE_DATE);
            id = flick.getString(TMDB_ID);
            original_title = flick.getString(TMDB_ORIGINAL_TITLE);
            backdrop_path = flick.getString(TMDB_BACKDROP_PATH);
            vote_average = flick.getString(TMDB_VOTE_AVERAGE);

            parsedMovieData[i] = poster_path + "\n\n\n" + original_title + "\n\n\n" +
                    release_date + "\n\n\n" + overview + "\n\n\n" + vote_average +
                    "\n\n\n" + id + "\n\n\n" + backdrop_path + "\n\n\n";

        }


        return parsedMovieData;
    }

}
