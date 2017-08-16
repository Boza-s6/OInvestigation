package com.example.oinvestigation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckNetworkConnectionWhenInBackground extends Service {
    private Thread mThread;

    public CheckNetworkConnectionWhenInBackground() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doStartService();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static final String TAG = CheckNetworkConnectionWhenInBackground.class.getSimpleName
            ().substring(0, 24);
    int threadId = 0;

    private void doStartService() {
        if (mThread != null) return;
        mThread = new Thread("MyThread" + threadId++) {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    Log.d(TAG, "From service: " + i++);
                    try {
                        sleep(4000);
                        Log.d(TAG, "trying to open connection");
                        URL url = new URL("https://google.com");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(10_000);
                        InputStream inputStream = conn.getInputStream();
                        String toString = isToString(inputStream);
                        Log.d(TAG, "part of received msg: " + toString.substring(0, 10).trim());
                        inputStream.close();
                        conn.disconnect();
                    } catch (InterruptedException | InterruptedIOException e) {
                        Log.d(TAG, "interrupted");
                        break;
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        Log.d(TAG, "IOException: " + e.toString());
                    }
                }
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
        };
        mThread.start();
    }
}
