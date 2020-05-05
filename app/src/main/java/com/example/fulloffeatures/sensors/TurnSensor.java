package com.example.fulloffeatures.sensors;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;

import static android.content.Context.SENSOR_SERVICE;

public class TurnSensor implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor acceleratorSensor;
    Context context;
    private long lastUpdate;
    private static int TOLERANCE = 25;

    public TurnSensor(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        acceleratorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 100) {
            lastUpdate = curTime;
            float[] values = event.values;
            double x = values[SensorManager.DATA_X];
            double y = values[SensorManager.DATA_Y];
            double z = values[SensorManager.DATA_Z];

            if (y > -TOLERANCE && y < TOLERANCE && z > 8 - TOLERANCE && z < 8 + TOLERANCE) {
                Toast.makeText(context, "turning phone off", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void poweron() {
        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");

        wakeLock.acquire();

        wakeLock.release();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
    }


    public void startListening() {
        sensorManager.registerListener(this, acceleratorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void setSensitivity(int sensitivity) {
        TOLERANCE = convert(sensitivity);
    }

    private int convert(int percent) {
        return 35 - percent / 5;
    }
}
