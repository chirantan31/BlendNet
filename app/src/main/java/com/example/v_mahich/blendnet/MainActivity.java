package com.example.v_mahich.blendnet;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    WifiReceiver wifiReceiver;
    IntentFilter intentFilter;
    final String TAG = "BLENDNETv0:MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startActivity(new Intent(this, ResourceListActivity.class));

        intentFilter = new IntentFilter();
        wifiReceiver = new WifiReceiver();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        //intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(wifiReceiver, intentFilter);




    }



}