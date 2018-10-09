package org.anderes.app.cookbook;


import org.anderes.app.cookbook.service.CookbookSyncJobService;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import static java.lang.Boolean.*;

class AppUtilities implements AppConfiguration {

    private final Context context;
    private final static String PREF_FULL_SYNC = "pref_fullSync";
    private final static String REF_BACKGROUND_SYNC = "pref_backgroundSync";
    private static final int SYNC_JOB_ID = 29;

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
        final Boolean backgroundSync = sharedPrefs.getBoolean(REF_BACKGROUND_SYNC, FALSE);
        Log.d("Sync", "SharedPreferences - Key: " + REF_BACKGROUND_SYNC + " - Result: " + backgroundSync);
        return backgroundSync;
    }

    @Override
    public void loadDefaultValuesForSettings() {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
    }

    @Override
    public void scheduleJob() {
        final ComponentName serviceComponent = new ComponentName(context, CookbookSyncJobService.class);
        final JobInfo.Builder builder = new JobInfo.Builder(SYNC_JOB_ID, serviceComponent);
        builder.setPeriodic(24 * 60 * 60 * 1000) // einmal im Tag
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
                //.setRequiresDeviceIdle(true); // device should be idle
                //.setRequiresCharging(false); // we don't care if the device is charging or not
        final JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        for (JobInfo info : jobScheduler.getAllPendingJobs()) {
            Log.v("Sync", "Job: " + info.getService());
        }
    }

    @Override
    public void cancelJob() {
        final JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.cancel(SYNC_JOB_ID);
        for (JobInfo info : jobScheduler.getAllPendingJobs()) {
            Log.v("Sync", "Job: " + info.getService());
        }
    }
}
