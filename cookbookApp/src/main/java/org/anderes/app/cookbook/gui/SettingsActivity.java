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

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            final Fragment prefFragment = new SettingsFragment();

            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, prefFragment)
                    .commit();
        }
    }

}
