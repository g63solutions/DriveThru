package com.zmediaz.apps.drivethru;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * Created by Computer on 1/1/2017.
 */

/*TODO 6 Create a Settings Fragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    //TODO 13 Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
    @Override
    public void onStart() {
        super.onStart();
        /* Register the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    //TODO 14 Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop
    @Override
    public void onStop() {
        super.onStop();
        /* Unregister the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /* TODO 12 Override onSharedPreferenceChanged to update
        non CheckBoxPreferences when they are changed steps 10 and 11 handle the list
        this handles if you have other types that need the summary updated.*/
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        {
            Preference preference = findPreference(key);
            if (null != preference) {
                if (!(preference instanceof CheckBoxPreference)) {
                    setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
                }
            }
        }

    }

    // TODO 7 Override onCreatePreferences and add
    // the preference_general_settings xml file using addPreferencesFromResource
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.pref_general_settings);

        // TODO 11 Set the preference summary on each preference
        // that isn't a CheckBoxPreference
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }


    }

    /*// TODO 10 Create a method called setPreferenceSummary that accepts a
     Preference and an Object and sets the summary of the preference. to do 10&11 looks for
     list summary preferences and sets them to the summary. Has to be done this way since the
     strings may get translated etc.
    */
    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        String key = preference.getKey();

        if (preference instanceof ListPreference) {
            /* For list preferences, look up the correct display value in */
            /* the preference's 'entries' list (since they have separate labels/values). */
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
    }
}
