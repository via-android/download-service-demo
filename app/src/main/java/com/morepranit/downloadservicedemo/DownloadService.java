package com.morepranit.downloadservicedemo;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadService extends IntentService {
    public static final String SERVICE_NAME = "com.morepranit.downloadservicedemo.DownloadService";
    private int RESULT_STATUS = Activity.RESULT_CANCELED;

    public DownloadService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra("url");
        String filename = intent.getStringExtra("filename");
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        if (file.exists()) {
            file.delete();
        }

        URL url1 = null;
        try {
            url1 = new URL(url);
            try (
                    InputStream stream = url1.openConnection().getInputStream();
                    InputStreamReader reader = new InputStreamReader(stream);
                    FileOutputStream outputStream = new FileOutputStream(file)
            ) {
                int next;
                while ((next = reader.read()) != -1) {
                    outputStream.write(next);
                }
                RESULT_STATUS = Activity.RESULT_OK;
                publishResult(RESULT_STATUS, file.getAbsolutePath());
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void publishResult(int status, String path) {
        Intent intent = new Intent(SERVICE_NAME);
        intent.putExtra("status", status);
        intent.putExtra("path", path);
        sendBroadcast(intent);
    }
}
