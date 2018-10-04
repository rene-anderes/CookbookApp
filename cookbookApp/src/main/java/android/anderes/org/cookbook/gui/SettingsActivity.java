package android.anderes.org.cookbook.gui;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            // Display the fragment as the main content.
            final Fragment prefFragment = new SettingsFragment();

            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, prefFragment)
                    .commit();
        }
    }

}
