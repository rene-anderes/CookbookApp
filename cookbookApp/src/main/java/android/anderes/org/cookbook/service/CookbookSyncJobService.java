package android.anderes.org.cookbook.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

public class CookbookSyncJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {

        Intent service = new Intent(getApplicationContext(), CookbookSyncService.class);
        getApplicationContext().startService(service);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
