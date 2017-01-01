package com.zmediaz.apps.drivethru;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Computer on 12/31/2016.
 */
// TODO (1) Add new Activity called Settings
public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);

        //TODO (2) Set setDisplayHomeAsUpEnabled to true on the support ActionBar
       /*This is needed since in the manifest there is no parent.  A parent activity
       * would also provide back support and the back arrow but it would only go to
       * the one parent screen.  This is not good for settings menus since they are
       * on multiple activities and you would keep navigating back to sam screen*/
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
