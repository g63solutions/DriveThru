package com.zmediaz.apps.drivethru.sync;


import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.zmediaz.apps.drivethru.R;

/**
 * Created by Computer on 1/21/2017.
 */

public class MovieFirebaseJobService extends JobService {


    String mKey = getString(R.string.api);

    private AsyncTask<Void, Void, Void> mFetchMovieTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mFetchMovieTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                MovieSyncTask.syncMovie(context, mKey);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };

        mFetchMovieTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchMovieTask != null) {
            mFetchMovieTask.cancel(true);
        }
        return true;
    }
}
