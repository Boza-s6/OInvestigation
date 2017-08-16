package com.example.oinvestigation;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class Broadcasts extends AppCompatActivity {
    private static final String TAG = Broadcasts.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcasts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        findViewById(R.id.explicit).setOnClickListener(view -> sendBroadcast(new Intent(Broadcasts.this, MyReceiverTest.class)));

        findViewById(R.id.Implicit).setOnClickListener(view -> sendBroadcast(new Intent(BuildConfig.APPLICATION_ID+".TEST")));

        findViewById(R.id.fanout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuildConfig.APPLICATION_ID+".TEST").putExtra("fanout", true);
                superPowerSendBroadcast(i);
            }

            private void superPowerSendBroadcast(Intent i) {
                Context context = Broadcasts.this;
                PackageManager pm = context.getPackageManager();
                List<ResolveInfo> matches = pm.queryBroadcastReceivers(i, 0);
                for (ResolveInfo resolveInfo : matches) {
                    Intent explicit = new Intent(i);
                    ComponentName cn =
                            new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
                                    resolveInfo.activityInfo.name);

                    explicit.setComponent(cn);
                    context.sendBroadcast(explicit);
                }
            }
        });

        findViewById(R.id.startBroadcastWithAlarm).setOnClickListener(view ->
                MyReceiver.startReciverinmins(getApplicationContext(), 2f));

        findViewById(R.id.startBroadcastWithHandler).setOnClickListener(view -> {
            Log.d(TAG, "starting MyReceiver with handler in 2 mins");
            new Handler().postDelayed(() -> sendBroadcast(new Intent(Broadcasts.this, MyReceiver.class)), 2*60*1000);
        });

        findViewById(R.id.startBroadcastWithAlarmAllowIdle).setOnClickListener(view ->
                MyReceiver.startReciverinminsAllowIdle(getApplicationContext(), 2f));

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, " registred receiver");
            Toast.makeText(getApplicationContext(), "registred receiver", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(BuildConfig.APPLICATION_ID+".TEST");
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }
}
