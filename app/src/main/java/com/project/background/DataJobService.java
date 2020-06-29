package com.project.background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import android.widget.Toast;

public class DataJobService extends JobService {
    private static final String TAG = "DataJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(JobParameters params){
        if(jobCancelled) return;

        /* Anadir el envio de coordenadas GPS */

        Toast.makeText(getApplicationContext(), "Doing background work", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Job finished");
        jobFinished(params, false);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "Job cancelled before completition");
        jobCancelled = true;
        return true;
    }
}
