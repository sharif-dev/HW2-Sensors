package com.example.fulloffeatures.sensors;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;


import static android.content.Context.SENSOR_SERVICE;

public class TurnSensor implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor acceleratorSensor;
    Context context;
    private long lastUpdate;
    private static int TOLERANCE = 4;

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
                Toast.makeText(context, "turning phone off", Toast.LENGTH_SHORT).show();
            }
        }
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
