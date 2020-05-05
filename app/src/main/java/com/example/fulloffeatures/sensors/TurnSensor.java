package com.example.fulloffeatures.sensors;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.util.Log;


import static android.content.Context.SENSOR_SERVICE;

public class TurnSensor implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor acceleratorSensor;
    Context context;
    private long lastUpdate;
    private static int TOLERANCE = 4;

    private final String TAG = "sensor";

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
            double y = values[SensorManager.DATA_Y];
            double z = values[SensorManager.DATA_Z];

            if (y > -TOLERANCE && y < TOLERANCE && z > 8 - TOLERANCE && z < 8 + TOLERANCE) {
                Log.d(TAG, "turning screen off");
                turnoff();
            }
        }
    }

    private void turnoff() {
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        assert manager != null;
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl =
                manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wl.acquire(10 * 60 * 1000L /*10 minutes*/);
        wl.release();
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
}
