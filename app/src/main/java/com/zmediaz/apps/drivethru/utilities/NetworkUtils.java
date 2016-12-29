package com.zmediaz.apps.drivethru.utilities;


import android.net.Uri;



import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Networking Class
 */

public class NetworkUtils {

    //    https://api.themoviedb.org/3/movie/popular?
// api_key=***SECRET***&language=en-US&page=1
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_LANGUAGE = "language";
    private static final String speak = "en-US";
    private final static String PARAM_PAGE = "page";
    private static final int page = 1;


    public static URL buildUrl(String selector, String key) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(selector)
                .appendQueryParameter(PARAM_API_KEY, key)
                .appendQueryParameter(PARAM_LANGUAGE, speak)
                .appendQueryParameter(PARAM_PAGE, Integer.toString(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
