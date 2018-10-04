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
            String versionName = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName;
            ((TextView)findViewById(R.id.about_version)).setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("About", e.getMessage());
        }
    }
}