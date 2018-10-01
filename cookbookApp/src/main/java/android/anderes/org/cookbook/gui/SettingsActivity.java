package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
