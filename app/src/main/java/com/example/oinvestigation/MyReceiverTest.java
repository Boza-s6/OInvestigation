package com.example.oinvestigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiverTest extends BroadcastReceiver {
    private static final String TAG = MyReceiverTest.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(TAG, "onReceive: Reciver started. IsFanout = " + intent.getBooleanExtra("fanout", false));
    }


}
