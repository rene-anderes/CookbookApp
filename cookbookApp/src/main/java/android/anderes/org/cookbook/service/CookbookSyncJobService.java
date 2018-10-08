package android.anderes.org.cookbook.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.anderes.org.cookbook.AppConstants.BROADCAST_SYNC_ACTION;

public class CookbookSyncJobService extends JobService {

    private BroadcastReceiver receiver;

    @Override
    public boolean onStartJob(JobParameters params) {

        final Intent service = new Intent(getApplicationContext(), CookbookSyncService.class);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                jobFinished(params, true);
                LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            }
        };

        final IntentFilter intentFilter = new IntentFilter(BROADCAST_SYNC_ACTION);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, intentFilter);
        getApplicationContext().startService(service);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}
