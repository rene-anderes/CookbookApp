package org.anderes.app.cookbook;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.anderes.app.cookbook.service.CookbookSyncJobService;

import static org.anderes.app.cookbook.AppConstants.LOG_TAG_SYNC;

public abstract class JobScheduleUtil {

    private static final int SYNC_JOB_ID = 29;
    private static final long INTERVAL = 24 * 60 * 60 * 1000; // einmal im Tag

    public static void scheduleJob(@NonNull Context context) {
        final ComponentName serviceComponent = new ComponentName(context, CookbookSyncJobService.class);
        final JobInfo.Builder builder = new JobInfo.Builder(SYNC_JOB_ID, serviceComponent);
        builder.setPeriodic(INTERVAL) // einmal im Tag
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //.setRequiresDeviceIdle(true); // device should be idle
        //.setRequiresCharging(false); // we don't care if the device is charging or not
        final JobInfo jobInfo = builder.build();
        final JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        Log.i(LOG_TAG_SYNC, "JobInfo: " + jobInfo.toString() + " - Interval: " + INTERVAL + "ms");
        logAllPendingJobs(context);
    }

    public static void cancelJob(@NonNull Context context) {
        final JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.cancel(SYNC_JOB_ID);
        logAllPendingJobs(context);
    }

    public static void logAllPendingJobs(@NonNull Context context) {
        final JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        Log.v(LOG_TAG_SYNC, "Get all pending jobs (context: " + context + ")");
        Log.v(LOG_TAG_SYNC, "job counter: " + jobScheduler.getAllPendingJobs().size());
        for (JobInfo info : jobScheduler.getAllPendingJobs()) {
            Log.v(LOG_TAG_SYNC, "Job: " + info.getService());
        }

    }
}
