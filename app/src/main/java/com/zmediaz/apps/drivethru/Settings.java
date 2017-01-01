package com.zmediaz.apps.drivethru;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Computer on 12/31/2016.
 */

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_settings);


       /*This is needed since in the manifest there is no parent.  A parent activity
       * would also provide back support and the back arrow but it would only go to
       * the one parent screen.  This is not good for settings menus since they are
       * on multiple activities and you would keep navigating back to sam screen*/
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*Then,
        in the actual SettingsActivity you should override the home button to act like the
        back button:

        if (id == android.R.id.home) {
        onBackPressed();}
        and you should display home as up, to allow up navigation:

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
