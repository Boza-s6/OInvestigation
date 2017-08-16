package com.example.oinvestigation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    public MyService() {
    }

    int i = 0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: " + "intent = [" + intent + "], flags =" +
                " [" + flags + "], startId = [" + startId + "]");

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called with: " + "");
        final Handler h = new Handler();
        JobInfo build = new JobInfo.Builder(11, new ComponentName(this, TestJobService.class))
                .setOverrideDeadline(0).build();
        getSystemService(JobScheduler.class).schedule(build);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startService(new Intent(MyService.this, MyService.class));
                if (i++ < 8) {
                    h.postDelayed(this, 10000);
                }
            }
        }, 10000);
    }

    class MyBinder extends Binder {
        public MyService getService() {return MyService.this;};
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: " + "intent = [" + intent + "]");
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called with: " + "");
        super.onDestroy();
//        startServiceInMinutes(this, 1);
    }

    public static void startServiceInMinutes(Context context, int mins) {
        Log.d(TAG, "Setting alarm in mins " + mins);
        Intent i = new Intent(context, MyService.class);
        i.setAction("PendingIntent");
        PendingIntent startService = PendingIntent.getService(context, 1, i, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis
                () + TimeUnit.MINUTES.toMillis(mins), startService);
    }
}
