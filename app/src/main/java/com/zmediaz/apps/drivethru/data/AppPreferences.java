package com.zmediaz.apps.drivethru.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.zmediaz.apps.drivethru.R;

/**
 * Created by Computer on 1/1/2017.
 */

public class AppPreferences {

    /*TODO 1 Write method for preference
     * String preferredSort = prefs.getString(keyForSort, defaultSort); Stores PReference
      * even though it looks like it only stores default*/
    public static String isPopular(Context context) {
        // COMPLETED (1) Return the user's preferred sort
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForSort = context.getString(R.string.pref_s_key);
        String defaultSort = context.getString(R.string.pref_s_v_popular);

        String preferredSort = prefs.getString(keyForSort, defaultSort);
        String popular = context.getString(R.string.pref_s_v_popular);
        String userSort;
        if (popular.equals(preferredSort)) {
            userSort = "popular";
        } else {
            userSort = "top_rated";
        }
        return userSort;
    }
}
