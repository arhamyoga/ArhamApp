package net.yoga.fcm;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MyJobService extends JobService {
    private static final String TAG = "MyJobService";

    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        return false;
    }

    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
