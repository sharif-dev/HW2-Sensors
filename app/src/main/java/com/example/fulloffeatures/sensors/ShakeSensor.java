package com.example.fulloffeatures.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import static android.content.Context.SENSOR_SERVICE;

public class ShakeSensor implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor acceleratorSensor;
    Context context;
    private long lastUpdate;
    private static final int SHAKE_THRESHOLD = 1;


    public ShakeSensor(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        acceleratorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, acceleratorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && event.values[0] > SHAKE_THRESHOLD) {
            Log.d("sensor", "shake detected w/ speed: " + event.values[0]);
            Toast.makeText(context, "shake detected w/ speed: " + event.values[0], Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
