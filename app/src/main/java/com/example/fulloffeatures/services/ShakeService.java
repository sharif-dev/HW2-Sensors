package com.example.fulloffeatures.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ShakeService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public ShakeService() {
        super("Shake Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SensorManager sensorManager;
        Sensor sensor;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

}
