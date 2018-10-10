package org.anderes.app.cookbook.gui;

import org.anderes.app.cookbook.ServiceLocatorProvider;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static java.lang.Boolean.FALSE;
import static org.anderes.app.cookbook.AppConstants.LOG_TAG_SYNC;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.OnSharedPreferenceChangeListener listener =
                    (sharedPrefs, key) -> {
                        if (key.equalsIgnoreCase("pref_backgroundSync")) {
                            final Boolean backgroundSync = sharedPrefs.getBoolean("pref_backgroundSync", FALSE);
                            Log.d(LOG_TAG_SYNC, "SharedPreferences - Key: pref_backgroundSync - Result: " + backgroundSync);
                            if (backgroundSync) {
                                ServiceLocatorProvider.getInstance().getAppConfiguration().scheduleJob();
                            } else {
                                ServiceLocatorProvider.getInstance().getAppConfiguration().cancelJob();
                            }
                        }
                    };
            prefs.registerOnSharedPreferenceChangeListener(listener);

            // Display the fragment as the main content.
            final Fragment prefFragment = new SettingsFragment();

            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, prefFragment)
                    .commit();
        }
    }

}
