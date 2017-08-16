package com.example.oinvestigation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = MyReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: starting service");
        context.startService(new Intent(context, MyService.class).setAction("fromBroadcast"));
    }

    public static void startReciverinminsAllowIdle(Context context, float mins) {
        Log.d(TAG, "Setting alarm for reciver (Allow idle)  in mins " + mins);
        Intent i = new Intent(context, MyReceiver.class);
        i.setAction("PendingIntent");
        PendingIntent startService = PendingIntent.getBroadcast(context, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis
                () + tomillis(mins), startService);
    }

    public static void startReciverinmins(Context context, float mins) {
        Log.d(TAG, "Setting alarm for reciver in mins " + mins);
        Intent i = new Intent(context, MyReceiver.class);
        i.setAction("PendingIntent");
        PendingIntent startService = PendingIntent.getBroadcast(context, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis
                () + tomillis(mins), startService);
    }

    private static long tomillis(float mins) {
        return (long) (mins * 60 * 1000);
    }
}
