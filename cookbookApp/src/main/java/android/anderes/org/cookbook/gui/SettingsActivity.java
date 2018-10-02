package android.anderes.org.cookbook.gui;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            //String syncConnPref = sharedPrefs.getString(SettingsActivity.pref_fullSync, "");

            // Display the fragment as the main content.
            final Fragment prefFragment = new SettingsFragment();

            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, prefFragment)
                    .commit();


            sharedPrefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                    Preference p = ((SettingsFragment) prefFragment).findPreference(key);

                    /*
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(key, newHighScore);
                    editor.apply();
                    */
                }
            });
        }
    }

}
