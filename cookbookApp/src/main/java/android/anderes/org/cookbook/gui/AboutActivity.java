package android.anderes.org.cookbook.gui;

import android.anderes.org.cookbook.R;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        try {
            final String versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            final String versionText = getResources().getString(R.string.about_version, versionName);
            ((TextView)findViewById(R.id.about_version)).setText(versionText);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("About", e.getMessage());
        }
    }
}