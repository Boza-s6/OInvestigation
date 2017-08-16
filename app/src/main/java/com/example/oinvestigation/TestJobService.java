package com.example.oinvestigation;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class TestJobService extends JobService {
    private static final String TAG = TestJobService.class.getSimpleName();
    private JobParameters mJobParameters;

    public TestJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mJobParameters = jobParameters;
        Log.d(TAG, "onStartJob + " + ReflectionUtils.getAllFields(jobParameters));
//        doLogging();
//        bindService()
        return true;
    }

    int i = 0;
    private void doLogging() {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "log + " + i++);
//                if (i < 10) {
//                    doLogging();
//                } else {
        Intent intent = new Intent(TestJobService.this, MyService.class).setAction("fromJobSetrvice");
        startService(intent);
        jobFinished(mJobParameters, false);
//                    Util.scheduleJob(TestJobService.this);
//                }
//            }
//        }, 10000); //every 10 secs
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob + " + jobParameters);
        return false;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
