package org.anderes.app.cookbook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.anderes.app.cookbook.JobScheduleUtil;
import static org.anderes.app.cookbook.AppConstants.*;

import static java.lang.Boolean.FALSE;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isBackgroundSync(context)) {
            Log.i(LOG_TAG_SYNC, "Start Sync-Job after boot of device.");
            JobScheduleUtil.scheduleJob(context);
        } else {
            Log.i(LOG_TAG_SYNC, "Sync-Job is not started after boot of device.");
        }

    }

    private boolean isBackgroundSync(Context context) {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final Boolean backgroundSync = sharedPrefs.getBoolean(PREF_BACKGROUND_SYNC, FALSE);
        Log.d(LOG_TAG_SYNC, "SharedPreferences - Key: " + PREF_BACKGROUND_SYNC + " - Result: " + backgroundSync);
        return backgroundSync;
    }
}
