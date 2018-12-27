package com.morepranit.downloadservicedemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvStatus;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra("status", Activity.RESULT_CANCELED);
            if (status == Activity.RESULT_OK) {
                tvStatus.setText("Download complete");
            } else {
                tvStatus.setText("Download failed");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tv_status);
    }

    public void onDownload(View view) {
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        intent.putExtra("url", "http://itvedant.com/index.php");
        intent.putExtra("filename", "index.php");
        startService(intent);
        tvStatus.setText("Downloading started...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadService.SERVICE_NAME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
