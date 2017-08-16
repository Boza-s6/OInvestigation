package com.example.oinvestigation;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by nemanja on 18.5.17..
 */

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private HandlerThread mThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mThread = new HandlerThread("HeartBeat thread") {
            @Override
            protected void onLooperPrepared() {
                doLogging();
            }
        };
//        mThread.start();
        registerReceiver(mReceiver, new IntentFilter(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED));
    }

    private void doLogging() {
        final Handler handler = new Handler(mThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "trying to open connection");
                    URL url = new URL("https://google.com");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10_000);
                    InputStream inputStream = conn.getInputStream();
                    String toString = isToString(inputStream);
                    Log.d(TAG, "part of received msg: " + toString.substring(0, 10).trim());
                    inputStream.close();
                    conn.disconnect();
                } catch (InterruptedIOException e) {
                    Log.d(TAG, "interrupted");
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    Log.d(TAG, "IOException: " + e.toString());
                }
                handler.postDelayed(this, 10_000);
            }
        });
    }

    private String isToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2000);
        InputStream is = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[100];
        int read;
        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        return baos.toString("UTF-8");
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager manager = getSystemService(PowerManager.class);
            boolean deviceIdleMode = manager.isDeviceIdleMode();
            Log.d(TAG, "on idle changed. Idle: " + deviceIdleMode);
        }
    };
}
