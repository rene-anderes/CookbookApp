package android.anderes.org.cookbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import static java.lang.Boolean.FALSE;

public class AppUtilities implements AppConfiguration {

    private final Context context;
    private final static String PREF_FULL_SYNC = "pref_fullSync";
    private final static String REF_BACKGROUND_SYNC = "pref_backgroundSync";

    public AppUtilities(Context context) {
        this.context = context;
    }

    @Override
    public boolean isOnline() {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean isFullSync() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final Boolean fullSync = sharedPrefs.getBoolean(PREF_FULL_SYNC, FALSE);
        Log.d("Sync", "SharedPreferences - Key: " + PREF_FULL_SYNC + " - Result: " + fullSync);
        return fullSync;
    }

    @Override
    public boolean isBackgroundSync() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final Boolean backgroundSync = sharedPrefs.getBoolean(REF_BACKGROUND_SYNC, FALSE);
        Log.d("Sync", "SharedPreferences - Key: " + REF_BACKGROUND_SYNC + " - Result: " + backgroundSync);
        return backgroundSync;
    }
}
