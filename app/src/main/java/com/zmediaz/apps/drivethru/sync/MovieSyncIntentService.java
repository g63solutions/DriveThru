package com.zmediaz.apps.drivethru.sync;

import android.app.IntentService;
import android.content.Intent;

import com.zmediaz.apps.drivethru.R;

/**
 * Created by Computer on 1/18/2017.
 */

public class MovieSyncIntentService extends IntentService {
    String mKey;

    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mKey = getString(R.string.api);

        MovieSyncTask.syncMovie(this, mKey);
    }
}
