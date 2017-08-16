package com.example.oinvestigation;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.MainThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show());

        findViewById(R.id.startJobSchedulere).setOnClickListener(view ->
                new Handler().postDelayed(() -> Util.scheduleJob(MainActivity.this), /*2 * 60 * */1000));

        findViewById(R.id.startBroadcastActivity).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Broadcasts.class)));

        findViewById(R.id.broadcast).setOnClickListener(v -> {
            Intent intent = new Intent("com.my.action");
            intent.addFlags(0x01000000);
            sendBroadcast(intent);
        });

        findViewById(R.id.startService).setOnClickListener(view -> startService());

        // no no
        findViewById(R.id.startServiceWithAlarm).setOnClickListener(view ->
                MyService.startServiceInMinutes(MainActivity.this, 2));

        // no no
        findViewById(R.id.startServiceWithHandler).setOnClickListener(view -> {
            Handler handler = new Handler();
            Log.i("MainActivity", "Starting service with delayed handler");
            handler.postDelayed(this::startService, TimeUnit.MINUTES.toMillis(2));
        });

        // no no
        findViewById(R.id.startServiceWithBroadcast).setOnClickListener(view -> {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent startBroadcastReciver = new Intent(MainActivity.this, MyReceiver.class);
                sendBroadcast(startBroadcastReciver);
            }, TimeUnit.MINUTES.toMillis(2));
        });

        findViewById(R.id.checkNetworkInbackground).setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CheckNetworkConnectionWhenInBackground
                    .class);
            startService(i);
        });
    }

    private void startService() {
        Intent startService = new Intent(MainActivity.this, MyService.class);
        startService.setAction("fromActivity");
        startService(startService);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
