package com.example.fulloffeatures.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.fulloffeatures.sensors.ShakeSensor;

public class ShakeService extends Service {

    ShakeSensor sensor;


    @Override
    public void onCreate() {
        super.onCreate();
        sensor = new ShakeSensor(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensor.startListening();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensor.stopListening();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
