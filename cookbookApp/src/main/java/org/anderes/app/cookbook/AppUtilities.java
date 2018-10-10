package org.anderes.app.cookbook;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import static java.lang.Boolean.*;
import static org.anderes.app.cookbook.AppConstants.*;

class AppUtilities implements AppConfiguration {

    private final Context context;

    AppUtilities(Context context) {
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
        final Boolean backgroundSync = sharedPrefs.getBoolean(PREF_BACKGROUND_SYNC, FALSE);
        Log.d("Sync", "SharedPreferences - Key: " + PREF_BACKGROUND_SYNC + " - Result: " + backgroundSync);
        return backgroundSync;
    }

    @Override
    public void loadDefaultValuesForSettings() {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
    }

    @Override
    public void scheduleJob() {
        JobScheduleUtil.scheduleJob(context);
    }

    @Override
    public void cancelJob() {
        JobScheduleUtil.cancelJob(context);
    }
}
